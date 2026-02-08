package br.com.fiap.inteligencia_service.dto;

import java.util.UUID;

public record EstabelecimentoSaudeDto(
    UUID id,
    String nome,
    String email,
    Boolean ativo
) {
}
