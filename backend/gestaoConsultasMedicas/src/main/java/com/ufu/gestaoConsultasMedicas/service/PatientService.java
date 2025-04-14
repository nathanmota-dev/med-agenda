package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.factory.PatientFactory;
import com.ufu.gestaoConsultasMedicas.repository.PatientRepository;
import com.ufu.gestaoConsultasMedicas.validation.PatientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Patient createPatient(String cpf, String name, LocalDate dateOfBirth, String address, String medicalHistory, String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        Patient patient = new Patient(cpf, email, hashedPassword, name, dateOfBirth, address, medicalHistory);
        return patientRepository.save(patient);
    }

    public Optional<Patient> authenticatePatient(String email, String rawPassword) {
        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isPresent() && passwordEncoder.matches(rawPassword, patient.get().getPassword())) {
            return patient;
        }
        return Optional.empty();
    }

    public Optional<Patient> getPatientByCpf(String cpf) {
        return patientRepository.findByCpf(cpf);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> updatePatient(String cpf, String name, LocalDate dateOfBirth, String address, String medicalHistory) {
        Optional<Patient> existingPatient = patientRepository.findByCpf(cpf);
        if (existingPatient.isPresent()) {
            PatientValidator.validateCpf(cpf);
            PatientValidator.validateDateOfBirth(dateOfBirth);
            PatientValidator.validateAge(dateOfBirth);

            // Atualiza os campos do paciente
            Patient patient = existingPatient.get();
            patient.setCpf(cpf);
            patient.setName(name);
            patient.setDateOfBirth(dateOfBirth);
            patient.setAddress(address);
            patient.setMedicalHistory(medicalHistory);

            // Salva as mudanças
            patientRepository.save(patient);
            return Optional.of(patient);
        }
        return Optional.empty();  // Paciente não encontrado
    }


    public boolean deletePatient(String cpf) {
        if (patientRepository.existsById(cpf)) {
            patientRepository.deleteById(cpf);
            return true;
        }
        return false;
    }
}
