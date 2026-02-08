package br.com.fiap.inteligencia_service.dto;

import java.util.List;

public record PaginacaoMovimentacaoDto(
    List<MovimentacaoDto> movimentacoes,
    PaginacaoDto dadosPaginacao
) {
}
