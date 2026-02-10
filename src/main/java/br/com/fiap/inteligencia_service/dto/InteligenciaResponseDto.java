package br.com.fiap.inteligencia_service.dto;

import java.util.List;

public record InteligenciaResponseDto(
    EstabelecimentoSaudeDto dadosUnidade,
    List<InteligenciaInsumoDto> insumosEmRisco
){}
