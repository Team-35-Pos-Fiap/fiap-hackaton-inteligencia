package br.com.fiap.inteligencia_service.services.interfaces;

import br.com.fiap.inteligencia_service.dto.InteligenciaResponseDto;

import java.util.List;
import java.util.UUID;

public interface InteligenciaService {
    InteligenciaResponseDto analisar(UUID idUnidade);
}
