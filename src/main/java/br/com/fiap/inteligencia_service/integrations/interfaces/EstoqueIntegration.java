package br.com.fiap.inteligencia_service.integrations.interfaces;

import br.com.fiap.inteligencia_service.dto.EstoqueDto;

import java.util.List;
import java.util.UUID;

public interface EstoqueIntegration {
    List<EstoqueDto> buscarTodoEstoquePorUnidade(UUID idUnidade);
    List<EstoqueDto> buscarTodoEstoquePorInsumo(UUID idInsumo);
}
