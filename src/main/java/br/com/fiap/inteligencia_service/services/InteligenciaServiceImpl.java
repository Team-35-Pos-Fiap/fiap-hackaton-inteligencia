package br.com.fiap.inteligencia_service.services;

import br.com.fiap.inteligencia_service.dto.*;
import br.com.fiap.inteligencia_service.engine.interfaces.AnaliseRiscoEngine;
import br.com.fiap.inteligencia_service.entities.enums.TipoMovimentacao;
import br.com.fiap.inteligencia_service.integrations.interfaces.EstabelecimentoSaudeIntegration;
import br.com.fiap.inteligencia_service.integrations.interfaces.EstoqueIntegration;
import br.com.fiap.inteligencia_service.integrations.interfaces.InsumoIntegration;
import br.com.fiap.inteligencia_service.integrations.interfaces.MovimentacaoIntegration;
import br.com.fiap.inteligencia_service.services.interfaces.InteligenciaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InteligenciaServiceImpl implements InteligenciaService {

    private static final int DIAS_JANELA = 90;
    private static final int DIAS_ALERTA = 14;

    private final EstoqueIntegration estoqueIntegration;
    private final MovimentacaoIntegration movimentacaoIntegration;
    private final EstabelecimentoSaudeIntegration estabelecimentoSaudeIntegration;
    private final InsumoIntegration insumoIntegration;
    private final AnaliseRiscoEngine analiseRiscoEngine;

    public InteligenciaServiceImpl(EstoqueIntegration estoqueIntegration, MovimentacaoIntegration movimentacaoIntegration, EstabelecimentoSaudeIntegration estabelecimentoSaudeIntegration, InsumoIntegration insumoIntegration, AnaliseRiscoEngine analiseRiscoEngine) {
        this.estoqueIntegration = estoqueIntegration;
        this.movimentacaoIntegration = movimentacaoIntegration;
        this.estabelecimentoSaudeIntegration = estabelecimentoSaudeIntegration;
        this.insumoIntegration = insumoIntegration;
        this.analiseRiscoEngine = analiseRiscoEngine;
    }

    private final Map<UUID, String> nomeCache = new ConcurrentHashMap<>();

    @Override
    public InteligenciaResponseDto analisar(UUID idUnidade) {
        LocalDate hoje = LocalDate.now();

        Map<UUID, InsumoDto> cacheInsumos = new ConcurrentHashMap<>();
        Map<UUID, EstabelecimentoSaudeDto> cacheUnidades = new ConcurrentHashMap<>();

        // 1. Busca Dados
        var itemsPorUnidade = estoqueIntegration.buscarTodoEstoquePorUnidade(idUnidade);
        var movimentos90d = movimentacaoIntegration.buscarPorUnidadeEPeriodo(idUnidade, hoje.minusDays(DIAS_JANELA), hoje);

        // 2. Prepara Dados
        var saidasValidasPorInsumo = movimentos90d.stream()
            .filter(m -> m.tipoMovimentacao().equals(TipoMovimentacao.SAIDA))
            .filter(m -> m.idTransferencia() == null)
            .collect(Collectors.groupingBy(MovimentacaoDto::idInsumo));

        // 3. Identifica Risco
        List<RiscoSnapshotDto> itemsEmRisco = itemsPorUnidade.stream()
            .map(item -> analiseRiscoEngine.calcularSnapshot(
                item,
                saidasValidasPorInsumo.getOrDefault(item.idInsumo(), List.of()),
                hoje
            ))
            .filter(snapshot -> List.of("ALTO", "MEDIO").contains(snapshot.risco()))
            .toList();

        // 4. Resolve Sugestões
        List<InteligenciaInsumoDto> insumosEmRisco = itemsEmRisco.parallelStream().map(alvo -> {
            var potenciais = estoqueIntegration.buscarTodoEstoquePorInsumo(alvo.insumoId()).stream()
                .filter(item -> !item.idUnidade().equals(alvo.unidadeId()))
                .toList();

            List<SugestaoTransferenciaDto> sugestoes = potenciais.parallelStream()
                .map(doador -> {
                    var movsDoador = movimentacaoIntegration.buscarPorUnidadeInsumoEPeriodo(
                        doador.idUnidade(), alvo.insumoId(), hoje.minusDays(DIAS_JANELA), hoje
                    );

                    var snapDoador = analiseRiscoEngine.calcularSnapshot(doador, movsDoador, hoje);

                    if ("BAIXO".equals(snapDoador.risco())) {
                        double qtdSugerida = Math.max(0, doador.quantidade() - (snapDoador.consumoDiario() * DIAS_ALERTA));
                        var dadosUnidade = cacheUnidades.computeIfAbsent(snapDoador.unidadeId(),
                            estabelecimentoSaudeIntegration::buscarEstabelecimentoPorId);
                        return new SugestaoTransferenciaDto(dadosUnidade, qtdSugerida);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

            return buildFinalReport(alvo, sugestoes, cacheInsumos);
        }).toList();

        var dadosUnidade = cacheUnidades.computeIfAbsent(idUnidade,
            estabelecimentoSaudeIntegration::buscarEstabelecimentoPorId);

        return new InteligenciaResponseDto(dadosUnidade, insumosEmRisco);
    }

    private InteligenciaInsumoDto buildFinalReport(RiscoSnapshotDto alvo, List<SugestaoTransferenciaDto> sugestoes, Map<UUID, InsumoDto> cacheInsumos) {
        double diasNecessarios = Math.max(0, DIAS_ALERTA - alvo.diasAteEsgotar());
        double quantidadeNecessaria = diasNecessarios * alvo.consumoDiario();

        var top5doadores = sugestoes.stream()
            .sorted(Comparator.comparing(SugestaoTransferenciaDto::quantidadeMaxima).reversed())
            .limit(5)
            .toList();

        var capacidadeDoacao = sugestoes.stream()
            .mapToDouble(SugestaoTransferenciaDto::quantidadeMaxima)
            .sum();

        SugestaoCompraDto compra = null;
        if (capacidadeDoacao < quantidadeNecessaria) {
            compra = new SugestaoCompraDto(quantidadeNecessaria - capacidadeDoacao);
        }

        var dadosInsumo = cacheInsumos.computeIfAbsent(alvo.insumoId(),
            insumoIntegration::buscarInsumoPorId);

        return new InteligenciaInsumoDto(
            dadosInsumo, alvo.risco(), alvo.diasAteEsgotar(),
            quantidadeNecessaria, alvo.tendenciaConsumo(), top5doadores, compra
        );
    }
}
