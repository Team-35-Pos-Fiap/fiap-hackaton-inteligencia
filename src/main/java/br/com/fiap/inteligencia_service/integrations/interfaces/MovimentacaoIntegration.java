package br.com.fiap.inteligencia_service.integrations.interfaces;

import br.com.fiap.inteligencia_service.dto.MovimentacaoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MovimentacaoIntegration {
    List<MovimentacaoDto> buscarPorUnidadeEPeriodo(UUID idUnidade, LocalDate inicio, LocalDate fim);
    List<MovimentacaoDto> buscarPorUnidadeInsumoEPeriodo(UUID idUnidade, UUID idInsumo, LocalDate inicio, LocalDate fim);
}
