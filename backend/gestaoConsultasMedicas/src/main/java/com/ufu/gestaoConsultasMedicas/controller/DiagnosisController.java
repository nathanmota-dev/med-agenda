package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.Diagnosis;
import com.ufu.gestaoConsultasMedicas.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    @PostMapping
    public ResponseEntity<?> createDiagnosis(@RequestBody Diagnosis diagnosis) {
        Optional<Diagnosis> created = diagnosisService.createDiagnosis(diagnosis);

        if (created.isPresent()) {
            return ResponseEntity.ok(created.get());
        } else {
            return ResponseEntity.badRequest().body("Consulta inválida.");
        }
    }

    @GetMapping("/consultation/{id}")
    public ResponseEntity<?> getDiagnosisByConsultationId(@PathVariable UUID id) {
        return diagnosisService.findByConsultationId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}