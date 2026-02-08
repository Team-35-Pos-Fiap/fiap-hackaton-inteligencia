package br.com.fiap.inteligencia_service.integrations;

import br.com.fiap.inteligencia_service.clients.EstoqueServiceClient;
import br.com.fiap.inteligencia_service.dto.EstoqueDto;
import br.com.fiap.inteligencia_service.dto.PaginacaoEstoqueDto;
import br.com.fiap.inteligencia_service.integrations.interfaces.EstoqueIntegration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;

@Component
public class EstoqueIntegrationImpl implements EstoqueIntegration {
    private final EstoqueServiceClient client;

    public EstoqueIntegrationImpl(EstoqueServiceClient client) {
        this.client = client;
    }

    public List<EstoqueDto> buscarTodoEstoquePorUnidade(UUID idUnidade) {
        return buscarPaginado((pagina) -> client.buscarPorUnidade(idUnidade, pagina));
    }

    public List<EstoqueDto> buscarTodoEstoquePorInsumo(UUID idInsumo) {
        return buscarPaginado((pagina) -> client.buscarPorInsumo(idInsumo, pagina));
    }

    private List<EstoqueDto> buscarPaginado(IntFunction<PaginacaoEstoqueDto> call) {
        List<EstoqueDto> estoque = new ArrayList<>();
        int paginaAtual = 1;
        int totalPaginas;

        do {
            var response = call.apply(paginaAtual);
            estoque.addAll(response.insumos());
            paginaAtual++;
            totalPaginas = response.dadosPaginacao().totalPaginas();
        } while (paginaAtual <= totalPaginas);

        return estoque;
    }
}
