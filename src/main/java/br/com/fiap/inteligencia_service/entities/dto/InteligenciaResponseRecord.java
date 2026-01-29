package br.com.fiap.inteligencia_service.entities.dto;

import java.util.List;
import java.util.UUID;

public record InteligenciaResponseRecord(
    UUID unidadeId,
    UUID insumoId,
    String risco,
    double diasAteEsgotar,
    double quantidadeNecessaria,
    List<SugestaoTransferenciaDTO> sugestoesTransferencia,
    SugestaoCompraDTO sugestaoCompra
){}
