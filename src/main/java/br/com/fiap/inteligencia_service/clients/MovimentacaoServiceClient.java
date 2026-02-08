package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.dto.PaginacaoMovimentacaoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.UUID;

@FeignClient(
    name="movimentacao-service",
    url="${services.movimentacao:}"
)
public interface MovimentacaoServiceClient {
    @GetMapping("/movimentacoes")
    PaginacaoMovimentacaoDto movimentacoesPorUnidadeInsumoEPeriodo(
        @RequestParam UUID idUnidade,
        @RequestParam UUID idInsumo,
        @RequestParam LocalDate dataInicio,
        @RequestParam LocalDate dataFim,
        @RequestParam int pagina
    );

    @GetMapping("/movimentacoes")
    PaginacaoMovimentacaoDto movimentacoesPorUnidadeEPeriodo(
        @RequestParam UUID idUnidade,
        @RequestParam LocalDate dataInicio,
        @RequestParam LocalDate dataFim,
        @RequestParam int pagina
    );
}
