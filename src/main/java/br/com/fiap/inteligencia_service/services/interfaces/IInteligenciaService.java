package br.com.fiap.inteligencia_service.services.interfaces;

import br.com.fiap.inteligencia_service.entities.dto.InteligenciaResponseRecord;

import java.util.List;
import java.util.UUID;

public interface IInteligenciaService {
    List<InteligenciaResponseRecord> analisar(UUID idUnidade);
}
