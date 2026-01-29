package br.com.fiap.inteligencia_service.controllers;

import br.com.fiap.inteligencia_service.services.interfaces.IInteligenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/inteligencia")
public class InteligenciaController {

    IInteligenciaService inteligenciaService;

    public InteligenciaController(IInteligenciaService inteligenciaService) {
        this.inteligenciaService = inteligenciaService;
    }

    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<Void> analisar(@PathVariable UUID idUnidade) {
        inteligenciaService.analisar(idUnidade);
        return ResponseEntity.noContent().build();
    }
}
