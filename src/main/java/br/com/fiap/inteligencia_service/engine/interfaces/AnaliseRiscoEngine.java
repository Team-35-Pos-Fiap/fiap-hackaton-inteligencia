package br.com.fiap.inteligencia_service.engine.interfaces;

import br.com.fiap.inteligencia_service.dto.EstoqueDto;
import br.com.fiap.inteligencia_service.dto.MovimentacaoDto;
import br.com.fiap.inteligencia_service.dto.RiscoSnapshotDto;

import java.time.LocalDate;
import java.util.List;

public interface AnaliseRiscoEngine {
    RiscoSnapshotDto calcularSnapshot(EstoqueDto item, List<MovimentacaoDto> movimetacoesSaida, LocalDate hoje);
}
