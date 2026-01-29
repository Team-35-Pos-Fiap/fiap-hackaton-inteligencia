package br.com.fiap.inteligencia_service.entities.dto;

import br.com.fiap.inteligencia_service.entities.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovimentacaoRecord(
    @NotNull UUID id,
    @NotNull TipoMovimentacao tipo,
    @NotNull UUID idInsumo,
    @NotNull UUID idUnidade,
    UUID idTransferencia,
    @Positive int quantidade,
    @NotNull LocalDateTime timestamp
) {
}
