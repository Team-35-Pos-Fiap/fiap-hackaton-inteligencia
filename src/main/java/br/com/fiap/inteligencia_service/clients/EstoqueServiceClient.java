package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.entities.dto.EstoqueRecord;
import br.com.fiap.inteligencia_service.entities.dto.PaginacaoEstoqueRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
    name = "estoque-service",
    url="${services.estoque}"
)
public interface EstoqueServiceClient {

    @GetMapping("/estoque/{idUnidade}/insumos")
    PaginacaoEstoqueRecord buscarPorUnidade(
        @PathVariable UUID idUnidade,
        @RequestParam int pagina
    );
}
