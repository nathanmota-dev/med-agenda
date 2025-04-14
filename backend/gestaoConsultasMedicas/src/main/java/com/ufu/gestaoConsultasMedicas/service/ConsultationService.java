package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.repository.ConsultationRepository;
import com.ufu.gestaoConsultasMedicas.repository.DoctorRepository;
import com.ufu.gestaoConsultasMedicas.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository,
                               DoctorRepository doctorRepository,
                               PatientRepository patientRepository, EmailService emailService) {
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
    }

    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDate date, boolean isUrgent, String observation) {
        Patient patientFromDb = patientRepository.findByCpf(patient.getCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente com CPF " + patient.getCpf() + " não encontrado."));

        Doctor doctorFromDb = doctorRepository.findByCrm(doctor.getCrm())
                .orElseThrow(() -> new IllegalArgumentException("Médico com CRM " + doctor.getCrm() + " não encontrado."));

        // Criação da consulta
        Consultation consultation = new Consultation();
        consultation.setConsultationId(UUID.randomUUID());
        consultation.setDate(date);
        consultation.setPatient(patientFromDb);
        consultation.setDoctor(doctorFromDb);
        consultation.setUrgent(isUrgent);
        consultation.setObservation(observation);

        // Envio de e-mail
        emailService.sendEmail(
                "delivered@resend.dev",
                "Consulta confirmada",
                "Sua consulta foi marcada para " + consultation.getDate()
        );

        return consultationRepository.save(consultation);
    }

    public Optional<Consultation> updateConsultation(UUID consultationId, LocalDate date, String observation) {
        return consultationRepository.findById(consultationId).map(consultation -> {
            consultation.setDate(date);
            consultation.setObservation(observation);
            return consultationRepository.save(consultation);
        });
    }

    public boolean cancelConsultation(UUID consultationId) {
        if (consultationRepository.existsById(consultationId)) {
            consultationRepository.deleteById(consultationId);
            return true;
        }
        return false;
    }

    public Optional<Consultation> getConsultationById(UUID consultationId) {
        return consultationRepository.findById(consultationId);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public List<Consultation> getPatientConsultationHistory(String patientCpf) {
        return consultationRepository.findByPatient_Cpf(patientCpf);
    }

}