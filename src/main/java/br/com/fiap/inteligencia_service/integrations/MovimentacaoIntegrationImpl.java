package br.com.fiap.inteligencia_service.integrations;

import br.com.fiap.inteligencia_service.clients.MovimentacaoServiceClient;
import br.com.fiap.inteligencia_service.dto.MovimentacaoDto;
import br.com.fiap.inteligencia_service.integrations.interfaces.MovimentacaoIntegration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MovimentacaoIntegrationImpl implements MovimentacaoIntegration {

    private final MovimentacaoServiceClient client;

    public MovimentacaoIntegrationImpl(MovimentacaoServiceClient client) {
        this.client = client;
    }

    public List<MovimentacaoDto> buscarPorUnidadeEPeriodo(UUID idUnidade, LocalDate inicio, LocalDate fim) {
        List<MovimentacaoDto> lista = new ArrayList<>();
        int pagina = 1;
        int total;
        do {
            var response = client.movimentacoesPorUnidadeEPeriodo(idUnidade, inicio, fim, pagina);
            lista.addAll(response.movimentacoes());
            pagina++;
            total = response.dadosPaginacao().totalPaginas();
        } while (pagina <= total);
        return lista;
    }

    public List<MovimentacaoDto> buscarPorUnidadeInsumoEPeriodo(UUID idUnidade, UUID idInsumo, LocalDate inicio, LocalDate fim) {
        List<MovimentacaoDto> lista = new ArrayList<>();
        int pagina = 1;
        int total;
        do {
            var response = client.movimentacoesPorUnidadeInsumoEPeriodo(idUnidade, idInsumo, inicio, fim, pagina);
            lista.addAll(response.movimentacoes());
            pagina++;
            total = response.dadosPaginacao().totalPaginas();
        } while (pagina <= total);
        return lista;
    }
}
