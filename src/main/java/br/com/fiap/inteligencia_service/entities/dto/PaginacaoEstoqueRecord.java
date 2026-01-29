package br.com.fiap.inteligencia_service.entities.dto;

import java.util.List;

public record PaginacaoEstoqueRecord (
    List<EstoqueRecord> items,
    PaginacaoRecord paginacao
){
}
