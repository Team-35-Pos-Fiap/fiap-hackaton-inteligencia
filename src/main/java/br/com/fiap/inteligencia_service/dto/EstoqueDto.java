package br.com.fiap.inteligencia_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record EstoqueDto(
    @NotNull UUID idUnidade,
    @NotNull UUID idInsumo,
    @Positive Integer quantidade
) {
}
