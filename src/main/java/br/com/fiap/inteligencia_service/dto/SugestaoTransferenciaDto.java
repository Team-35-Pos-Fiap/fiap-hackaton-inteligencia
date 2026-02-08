package br.com.fiap.inteligencia_service.dto;

public record SugestaoTransferenciaDto(
    EstabelecimentoSaudeDto dadosUnidade,
    double quantidadeMaxima
) {
}
