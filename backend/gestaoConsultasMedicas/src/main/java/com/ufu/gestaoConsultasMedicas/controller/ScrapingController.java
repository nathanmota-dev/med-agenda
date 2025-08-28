package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.HabilidadeMedica;
import com.ufu.gestaoConsultasMedicas.service.ScrapingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/scrape")
public class ScrapingController {

    private final ScrapingService scrapingService;

    public ScrapingController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @GetMapping("/extrair-e-salvar")
    public ResponseEntity<List<HabilidadeMedica>> scrapeESalvaHabilidades() {
        try {
            List<HabilidadeMedica> habilidades = scrapingService.extrairESalvarHabilidades();
            return ResponseEntity.ok(habilidades);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<HabilidadeMedica>> listarHabilidadesDoBanco() {
        try {
            List<HabilidadeMedica> habilidades = scrapingService.listarDoBanco();
            return ResponseEntity.ok(habilidades);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // NOVO ENDPOINT ADICIONADO AQUI
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalHabilidades() {
        try {
            long total = scrapingService.contarHabilidades();
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}