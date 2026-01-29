package br.com.fiap.inteligencia_service.entities.dto;

import java.util.UUID;

public record RiscoSnapshot(
    UUID unidadeId,
    UUID insumoId,
    int quantidade,
    double consumoDiario,
    double diasAteEsgotar,
    String risco
){}
