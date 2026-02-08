package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.dto.EstabelecimentoSaudeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "estabelecimento-saude",
    url="${services.estabelecimento-saude:}"
)
public interface EstabelecimentoSaudeServiceClient {
    @GetMapping("/estabelecimento-saude/estabelecimentos/{idEstabelecimento}")
    EstabelecimentoSaudeDto buscarEstabelecimentoPorId(@PathVariable UUID idEstabelecimento);
}
