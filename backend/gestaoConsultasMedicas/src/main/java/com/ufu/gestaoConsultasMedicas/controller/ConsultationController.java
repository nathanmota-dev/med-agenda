package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @Autowired
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Consultation> scheduleConsultation(
            @RequestBody Consultation consultation) {
        Consultation createdConsultation = consultationService.createConsultation(
                consultation.getPatient(),
                consultation.getDoctor(),
                consultation.getDateTime(),
                consultation.isUrgent(),
                consultation.getObservation()
        );
        return ResponseEntity.ok(createdConsultation);
    }

    @PutMapping("/update")
    public ResponseEntity<Consultation> updateConsultation(
            @RequestBody Consultation consultationUpdateRequest) {

        Optional<Consultation> updatedConsultation = consultationService.updateConsultation(
                consultationUpdateRequest.getConsultationId(),
                consultationUpdateRequest.getDateTime(),
                consultationUpdateRequest.getObservation()
        );

        return updatedConsultation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable("id") UUID consultationId) {
        Optional<Consultation> consultation = consultationService.getConsultationById(consultationId);
        return consultation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Consultation>> getAllConsultations() {
        List<Consultation> consultations = consultationService.getAllConsultations();
        return ResponseEntity.ok(consultations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelConsultation(@PathVariable UUID id) {
        boolean cancelled = consultationService.cancelConsultation(id);
        if (cancelled) {
            return ResponseEntity.ok("Consulta cancelada com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patient-history/{cpf}")
    public ResponseEntity<List<Consultation>> getPatientConsultationHistory(@PathVariable String cpf) {
        List<Consultation> consultations = consultationService.getPatientConsultationHistory(cpf);
        return ResponseEntity.ok(consultations);
    }
}
