package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Patient loginRequest) {
        Optional<Patient> patient = patientService.authenticatePatient(loginRequest.getEmail(), loginRequest.getPassword());
        if (patient.isPresent()) {
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid email or password.");
    }

    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient newPatient = patientService.createPatient(
                patient.getCpf(),
                patient.getName(),
                patient.getDateOfBirth(),
                patient.getAddress(),
                patient.getMedicalHistory(),
                patient.getEmail(),
                patient.getPassword()
        );
        return ResponseEntity.ok(newPatient);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String cpf) {
        Optional<Patient> patient = patientService.getPatientByCpf(cpf);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/update/{cpf}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable String cpf,
            @RequestBody Patient patient) {

        Optional<Patient> updatedPatient = patientService.updatePatient(
                cpf,
                patient.getName(),
                patient.getDateOfBirth(),
                patient.getAddress(),
                patient.getMedicalHistory()
        );
        return updatedPatient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{cpf}")
    public ResponseEntity<String> deletePatient(@PathVariable String cpf) {
        boolean deleted = patientService.deletePatient(cpf);
        if (deleted) {
            return ResponseEntity.ok("Paciente deletado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
