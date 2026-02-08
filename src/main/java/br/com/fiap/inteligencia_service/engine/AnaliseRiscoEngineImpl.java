package br.com.fiap.inteligencia_service.engine;

import br.com.fiap.inteligencia_service.dto.ConsumoInfoDto;
import br.com.fiap.inteligencia_service.dto.EstoqueDto;
import br.com.fiap.inteligencia_service.dto.MovimentacaoDto;
import br.com.fiap.inteligencia_service.dto.RiscoSnapshotDto;
import br.com.fiap.inteligencia_service.engine.interfaces.AnaliseRiscoEngine;
import br.com.fiap.inteligencia_service.entities.enums.Riscos;
import br.com.fiap.inteligencia_service.entities.enums.Tendencias;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class AnaliseRiscoEngineImpl implements AnaliseRiscoEngine {
    private static final int DIAS_RISCOALTO = 7;
    private static final int DIAS_ALERTA = 14;

    @Override
    public RiscoSnapshotDto calcularSnapshot(EstoqueDto item, List<MovimentacaoDto> movimentacoesSaida, LocalDate hoje) {
        ConsumoInfoDto consumoInfoDto = calcularConsumoDiarioPonderado(movimentacoesSaida, hoje);

        if (consumoInfoDto.consumoDiarioPonderado() == 0){
            return new RiscoSnapshotDto(
                item.idUnidade(), item.idInsumo(), item.quantidade(), 0, null, "SEM_CONSUMO", Tendencias.UNKNOWN.toString()
            );
        }

        double diasAteEsgotar = item.quantidade() / consumoInfoDto.consumoDiarioPonderado();
        String risco = classificarRisco(diasAteEsgotar);

        return new RiscoSnapshotDto(
            item.idUnidade(), item.idInsumo(), item.quantidade(),
            consumoInfoDto.consumoDiarioPonderado(), diasAteEsgotar, risco, consumoInfoDto.tendencia()
        );
    }

    private ConsumoInfoDto calcularConsumoDiarioPonderado(List<MovimentacaoDto> movimentacoes, LocalDate hoje) {
        double consumo7  = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(7), hoje);
        double consumo30 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(30), hoje);
        double consumo60 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(60), hoje);
        double consumo90 = calcularConsumoMedioPorPeriodo(movimentacoes, hoje.minusDays(90), hoje);

        double ponderado = 0.4 * consumo7 + 0.3 * consumo30 + 0.2 * consumo60 + 0.1 * consumo90;
        String tendencia = calcularTendencia(consumo7, consumo30);

        return new ConsumoInfoDto(ponderado, tendencia);
    }

    private double calcularConsumoMedioPorPeriodo(List<MovimentacaoDto> movimentacoes, LocalDate dataInicio, LocalDate hoje) {
        double totalSaidas = movimentacoes.stream()
            .filter(m -> !m.timestamp().isBefore(dataInicio.atStartOfDay()))
            .mapToDouble(MovimentacaoDto::quantidade)
            .sum();

        long dias = ChronoUnit.DAYS.between(dataInicio, hoje) + 1;
        return dias > 0 ? totalSaidas / dias : 0;
    }

    private String calcularTendencia(double consumo7, double consumo30) {
        if(consumo7 == 0 && consumo30 == 0) return Tendencias.UNKNOWN.toString();
        if (consumo30 == 0) return Tendencias.UP.toString();

        double ratio = consumo7 / consumo30;

        if (ratio > 1.25) return Tendencias.UP.toString();
        if (ratio < 0.75) return Tendencias.DOWN.toString();
        return Tendencias.STABLE.toString();
    }

    private String classificarRisco(double diasAteEsgotar) {
        if (diasAteEsgotar < DIAS_RISCOALTO) return Riscos.ALTO.toString();
        else if (diasAteEsgotar < DIAS_ALERTA) return Riscos.MEDIO.toString();
        else return Riscos.BAIXO.toString();
    }
}
