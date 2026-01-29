package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.entities.dto.MovimentacaoRecord;
import br.com.fiap.inteligencia_service.entities.dto.PaginacaoMovimentacaoRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(
    name="movimentacao-service",
    url="${services.movimentacao}"
)
public interface MovimentacaoServiceClient {

    @GetMapping("/movimentacoes/unidade/{idUnidade}")
    PaginacaoMovimentacaoRecord movimentacoesPorUnidadeInsumosEPeriodo(
        @PathVariable UUID idUnidade,
        @RequestParam UUID idInsumo,
        @RequestParam LocalDate dataInicio,
        @RequestParam LocalDate dataFim,
        @RequestParam int pagina
    );
}
