package br.com.fiap.inteligencia_service.dto;

public record PaginacaoDto(
    Integer paginaAtual, Integer totalPaginas, Integer totalItens
) {
}
