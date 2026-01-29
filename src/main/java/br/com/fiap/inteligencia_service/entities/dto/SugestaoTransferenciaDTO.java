package br.com.fiap.inteligencia_service.entities.dto;

import java.util.UUID;

public record SugestaoTransferenciaDTO(
    UUID unidadeDoadora,
    double quantidadeMaxima
) {
}
