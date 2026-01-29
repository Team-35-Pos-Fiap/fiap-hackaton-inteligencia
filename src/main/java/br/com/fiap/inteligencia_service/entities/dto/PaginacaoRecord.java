package br.com.fiap.inteligencia_service.entities.dto;

public record PaginacaoRecord(
    Integer paginaAtual, Integer totalPaginas, Integer totalItens
) {
}
