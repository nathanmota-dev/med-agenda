package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.service.GuiaVigilanciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guia")
public class GuiaVigilanciaController {

    private final GuiaVigilanciaService service;

    public GuiaVigilanciaController(GuiaVigilanciaService service) {
        this.service = service;
    }

    @GetMapping("/termos")
    public ResponseEntity<?> listarSomenteDoBanco() {
        try {
            var result = service.listarDoBanco();
            return ResponseEntity.ok(result); // [] se vazio
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", String.valueOf(e.getMessage())
            ));
        }
    }

    @PostMapping("/termos/refresh")
    public ResponseEntity<?> refreshDoPdf() {
        try {
            var result = service.extrairSalvarERetornar();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", String.valueOf(e.getMessage())
            ));
        }
    }
}
