package br.com.fiap.inteligencia_service.entities.dto;

import java.util.List;

public record PaginacaoMovimentacaoRecord(
    List<MovimentacaoRecord> movimentacoes,
    PaginacaoRecord paginacao
) {
}
