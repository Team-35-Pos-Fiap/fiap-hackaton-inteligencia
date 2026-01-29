package br.com.fiap.inteligencia_service.clients;

import br.com.fiap.inteligencia_service.entities.dto.InsumoRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "insumo-service",
    url = "${services.insumos}"
)
public interface InsumoServiceClient {

    @GetMapping("/insumos/{idInsumo}")
    InsumoRecord buscarInsumo(@PathVariable UUID idInsumo);
}
