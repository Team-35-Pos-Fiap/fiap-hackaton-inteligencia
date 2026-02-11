package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.config.FeignConfig;
import br.com.fiap.inteligencia_service.dto.InsumoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "insumos",
    configuration = FeignConfig.class
)
public interface InsumoServiceClient {

    @GetMapping("/insumos/{idInsumo}")
    InsumoDto buscarInsumoPorId(@PathVariable UUID idInsumo);

}
