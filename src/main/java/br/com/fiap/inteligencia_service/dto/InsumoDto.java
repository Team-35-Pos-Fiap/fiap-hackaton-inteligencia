package br.com.fiap.inteligencia_service.dto;

import java.util.UUID;

public record InsumoDto(
    UUID id,
    String nome,
    String descricao
) {
}
