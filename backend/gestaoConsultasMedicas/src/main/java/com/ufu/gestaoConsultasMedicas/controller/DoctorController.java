package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.service.DoctorService;
import com.ufu.gestaoConsultasMedicas.strategy.SearchDoctorByCrm;
import com.ufu.gestaoConsultasMedicas.strategy.SearchDoctorByName;
import com.ufu.gestaoConsultasMedicas.strategy.SearchDoctorBySpecialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Doctor loginRequest) {
        Optional<Doctor> doctor = doctorService.authenticateDoctor(loginRequest.getEmail(), loginRequest.getPassword());
        if (doctor.isPresent()) {
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid email or password.");
    }

    @PostMapping("/create")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor newDoctor = doctorService.addDoctor(doctor);
        return ResponseEntity.ok(newDoctor);
    }

    @PutMapping("/{crm}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable String crm, @RequestBody Doctor updatedDoctor) {
        Optional<Doctor> doctor = doctorService.updateDoctor(crm, updatedDoctor);
        return doctor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{crm}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String crm) {
        boolean deleted = doctorService.deleteDoctor(crm);
        if (deleted) {
            return ResponseEntity.ok("Doutor deletado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(
            @RequestParam(required = false) String crm,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty) {

        // Definindo a estratégia de busca com base no parâmetro fornecido
        if (crm != null) {
            doctorService.setSearchStrategy(new SearchDoctorByCrm());
            return ResponseEntity.ok(doctorService.searchDoctors(crm));
        } else if (name != null) {
            doctorService.setSearchStrategy(new SearchDoctorByName());
            return ResponseEntity.ok(doctorService.searchDoctors(name));
        } else if (specialty != null) {
            doctorService.setSearchStrategy(new SearchDoctorBySpecialty());
            return ResponseEntity.ok(doctorService.searchDoctors(specialty));
        } else {
            return ResponseEntity.badRequest().body(null);  // Retorna 400 se nenhum parâmetro for passado
        }
    }

    @GetMapping("/consultations/{crm}")
    public ResponseEntity<List<LocalDate>> getConsultationDatesByDoctorCrm(@PathVariable String crm) {
        List<LocalDate> dates = doctorService.getConsultationDatesByCrm(crm);
        return ResponseEntity.ok(dates);
    }
}
