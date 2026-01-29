package br.com.fiap.inteligencia_service.services;

import br.com.fiap.inteligencia_service.clients.EstoqueServiceClient;
import br.com.fiap.inteligencia_service.clients.InsumoServiceClient;
import br.com.fiap.inteligencia_service.clients.MovimentacaoServiceClient;
import br.com.fiap.inteligencia_service.entities.dto.*;
import br.com.fiap.inteligencia_service.entities.enums.TipoMovimentacao;
import br.com.fiap.inteligencia_service.services.interfaces.IInteligenciaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InteligenciaService implements IInteligenciaService {

    private final EstoqueServiceClient estoqueServiceClient;
    private final InsumoServiceClient insumoServiceClient;
    private final MovimentacaoServiceClient movimentacaoServiceClient;

    public InteligenciaService(EstoqueServiceClient estoqueServiceClient, InsumoServiceClient insumoServiceClient, MovimentacaoServiceClient movimentacaoServiceClient) {
        this.estoqueServiceClient = estoqueServiceClient;
        this.insumoServiceClient = insumoServiceClient;
        this.movimentacaoServiceClient = movimentacaoServiceClient;
    }

    @Override
    public List<InteligenciaResponseRecord> analisar(UUID idUnidade) {

        List<EstoqueRecord> itemsPorUnidade = buscarListaDeInsumosPorUnidade(idUnidade);

        List<RiscoSnapshot> snapshot = itemsPorUnidade.stream()
            .map(item -> calcularSnapshotInsumo(idUnidade, item))
            .toList();

        return gerarRespostas(snapshot);
    }

    private List<EstoqueRecord> buscarListaDeInsumosPorUnidade(UUID idUnidade) {
        List<EstoqueRecord> estoque = new ArrayList<>();
        int paginaAtual = 1;
        int totalPaginas;

        do {
            var response = estoqueServiceClient.buscarPorUnidade(idUnidade, paginaAtual);
            estoque.addAll(response.items());
            paginaAtual++;
            totalPaginas = response.paginacao().totalPaginas();
        } while (paginaAtual <= totalPaginas);

        return estoque;
    }

    private RiscoSnapshot calcularSnapshotInsumo(UUID idUnidade, EstoqueRecord item) {

        // Buscar movimentações últimos 90 dias
        List<MovimentacaoRecord> movimentacoes =
            buscarListaDeMovimentacoesPorUnidadeEInsumoDosUltimos90Dias(idUnidade, item.idInsumo())
                .stream()
                .filter(m -> m.tipo().equals(TipoMovimentacao.SAIDA))
                .filter(m -> m.idTransferencia() == null)
                .toList();

        // Consumo médio diário
        double consumoDiario = calcularConsumoDiarioPonderado(movimentacoes);
        consumoDiario = consumoDiario == 0 ? 0.0001 : consumoDiario;

        double diasAteEsgotar = item.quantidade() / consumoDiario;
        String risco = classificarRisco(diasAteEsgotar);

        return new RiscoSnapshot(
            idUnidade,
            item.idInsumo(),
            item.quantidade(),
            consumoDiario,
            diasAteEsgotar,
            risco
        );
    }

    private List<MovimentacaoRecord> buscarListaDeMovimentacoesPorUnidadeEInsumoDosUltimos90Dias(UUID idUnidade, UUID idInsumo) {
        List<MovimentacaoRecord> movimentacoes = new ArrayList<>();
        int paginaAtual = 1;
        int totalPaginas;
        LocalDate hoje = LocalDate.now();

        do {
            var response = movimentacaoServiceClient.movimentacoesPorUnidadeInsumosEPeriodo(
                idUnidade, idInsumo, hoje.minusDays(90), hoje, paginaAtual
            );
            movimentacoes.addAll(response.movimentacoes());
            paginaAtual++;
            totalPaginas = response.paginacao().totalPaginas();
        } while (paginaAtual <= totalPaginas);

        return movimentacoes;
    }

    private double calcularConsumoDiarioPonderado(List<MovimentacaoRecord> movimentacoes) {
        LocalDate hoje = LocalDate.now();

        double consumo7  = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(7));
        double consumo30 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(30));
        double consumo60 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(60));
        double consumo90 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(90));

        // Evitar divisão por zero
        consumo7  = consumo7 == 0 ? 0.0001 : consumo7;
        consumo30 = consumo30 == 0 ? 0.0001 : consumo30;
        consumo60 = consumo60 == 0 ? 0.0001 : consumo60;
        consumo90 = consumo90 == 0 ? 0.0001 : consumo90;

        // Combinar períodos ponderados
        return 0.4*consumo7 + 0.3*consumo30 + 0.2*consumo60 + 0.1*consumo90;
    }

    private double calcularConsumoMedioPorPeriodo(List<MovimentacaoRecord> movimentacoes, LocalDate dataInicio) {
        double totalSaidas = movimentacoes.stream()
            .filter(m -> m.timestamp().isAfter(dataInicio.atStartOfDay()))
            .mapToDouble(MovimentacaoRecord::quantidade)
            .sum();

        long dias = ChronoUnit.DAYS.between(dataInicio, LocalDate.now()) + 1;
        return dias > 0 ? totalSaidas / dias : 0;
    }

    private String classificarRisco(double diasAteEsgotar) {
        if (diasAteEsgotar < 7) return "ALTO";
        else if (diasAteEsgotar < 14) return "MEDIO";
        else return "BAIXO";
    }

    private List<InteligenciaResponseRecord> gerarRespostas(List<RiscoSnapshot> snapshot) {

        List<InteligenciaResponseRecord> resposta = new ArrayList<>();

        // Agrupar por insumo
        Map<UUID, List<RiscoSnapshot>> porInsumo = snapshot.stream()
            .collect(Collectors.groupingBy(RiscoSnapshot::insumoId));

        for (Map.Entry<UUID, List<RiscoSnapshot>> entry : porInsumo.entrySet()) {

            List<RiscoSnapshot> lista = entry.getValue();

            // ALTO risco
            List<RiscoSnapshot> criticos = lista.stream()
                .filter(s -> s.risco().equals("ALTO"))
                .toList();

            // Doadores (dias > 14)
            List<RiscoSnapshot> doadores = lista.stream()
                .filter(s -> s.diasAteEsgotar() > 14)
                .sorted(Comparator.comparing(RiscoSnapshot::diasAteEsgotar).reversed())
                .toList();

            for (RiscoSnapshot alvo : criticos) {

                // 1️⃣ Quantidade necessária para sair do risco ALTO (target BAIXO)
                double diasNecessarios = 14 - alvo.diasAteEsgotar();
                double quantidadeNecessaria = diasNecessarios * alvo.consumoDiario();

                // 2️⃣ Sugestões de transferência
                List<SugestaoTransferenciaDTO> sugestoes = new ArrayList<>();
                double capacidadeTotal = 0;

                for (RiscoSnapshot doador : doadores) {

                    double excessoDias = doador.diasAteEsgotar() - 14;
                    double qtdMax = excessoDias * doador.consumoDiario();

                    if (qtdMax > 0) {
                        sugestoes.add(new SugestaoTransferenciaDTO(doador.unidadeId(), qtdMax));
                        capacidadeTotal += qtdMax;
                    }
                }

                SugestaoCompraDTO compra = null;
                if (capacidadeTotal < quantidadeNecessaria) {
                    compra = new SugestaoCompraDTO(quantidadeNecessaria - capacidadeTotal);
                }

                resposta.add(new InteligenciaResponseRecord(
                    alvo.unidadeId(),
                    alvo.insumoId(),
                    alvo.risco(),
                    alvo.diasAteEsgotar(),
                    quantidadeNecessaria,
                    sugestoes,
                    compra
                ));
            }
        }

        return resposta;
    }
}
