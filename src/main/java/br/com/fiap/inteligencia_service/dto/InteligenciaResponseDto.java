package br.com.fiap.inteligencia_service.dto;

import java.util.List;

public record InteligenciaResponseDto(
    EstabelecimentoSaudeDto dadosUnidade,
    InsumoDto dadosInsumo,
    String risco,
    Double diasAteEsgotar,
    double quantidadeNecessaria,
    String tendenciaConsumo,
    List<SugestaoTransferenciaDto> sugestoesTransferencia,
    SugestaoCompraDto sugestaoCompra
){}
