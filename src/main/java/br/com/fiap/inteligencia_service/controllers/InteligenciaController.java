package br.com.fiap.inteligencia_service.controllers;

import br.com.fiap.inteligencia_service.dto.InteligenciaResponseDto;
import br.com.fiap.inteligencia_service.services.interfaces.InteligenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inteligencia")
public class InteligenciaController {

    InteligenciaService inteligenciaService;

    public InteligenciaController(InteligenciaService inteligenciaService) {
        this.inteligenciaService = inteligenciaService;
    }

    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<InteligenciaResponseDto> analisar(@PathVariable UUID idUnidade) {

        return ResponseEntity.ok(this.inteligenciaService.analisar(idUnidade));
    }
}
