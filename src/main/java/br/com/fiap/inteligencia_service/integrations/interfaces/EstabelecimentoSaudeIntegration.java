package br.com.fiap.inteligencia_service.integrations.interfaces;

import br.com.fiap.inteligencia_service.dto.EstabelecimentoSaudeDto;

import java.util.UUID;

public interface EstabelecimentoSaudeIntegration {
    EstabelecimentoSaudeDto buscarEstabelecimentoPorId(UUID idEstabelecimento);
}
