package br.com.fiap.inteligencia_service.dto;

import java.util.UUID;

public record RiscoSnapshotDto(
    UUID unidadeId,
    UUID insumoId,
    int quantidade,
    double consumoDiario,
    Double diasAteEsgotar,
    String risco,
    String tendenciaConsumo
){}
