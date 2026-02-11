package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.config.FeignConfig;
import br.com.fiap.inteligencia_service.dto.PaginacaoEstoqueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
    name = "estoque-service",
    configuration = FeignConfig.class
)
public interface EstoqueServiceClient {

    @GetMapping("/estoque/{idUnidade}")
    PaginacaoEstoqueDto buscarPorUnidade(
        @PathVariable UUID idUnidade,
        @RequestParam int pagina
    );

    @GetMapping("/estoque/insumos/{idInsumo}")
    PaginacaoEstoqueDto buscarPorInsumo(
        @PathVariable UUID idInsumo,
        @RequestParam int pagina
    );
}
