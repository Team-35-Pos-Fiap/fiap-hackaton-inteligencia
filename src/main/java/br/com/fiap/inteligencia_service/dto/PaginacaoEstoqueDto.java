package br.com.fiap.inteligencia_service.dto;

import java.util.List;

public record PaginacaoEstoqueDto(
    List<EstoqueDto> insumos,
    PaginacaoDto dadosPaginacao
){
}
