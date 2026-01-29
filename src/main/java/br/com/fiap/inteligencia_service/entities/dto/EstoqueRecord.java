package br.com.fiap.inteligencia_service.entities.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record EstoqueRecord(
    @NotNull UUID idUnidade,
    @NotNull UUID idInsumo,
    @Positive Integer quantidade
) {
}
