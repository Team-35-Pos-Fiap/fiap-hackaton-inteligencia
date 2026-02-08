package br.com.fiap.inteligencia_service.integrations.interfaces;

import br.com.fiap.inteligencia_service.dto.InsumoDto;

import java.util.UUID;

public interface InsumoIntegration {
    InsumoDto buscarInsumoPorId(UUID idInsumo);
}
