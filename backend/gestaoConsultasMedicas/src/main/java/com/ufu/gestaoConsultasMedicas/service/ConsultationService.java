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

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository,
                               DoctorRepository doctorRepository,
                               PatientRepository patientRepository) {
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDate date, boolean isUrgent, String observation) {
        // Verifique se o médico e o paciente existem
        if (!doctorRepository.existsById(doctor.getCrm())) {
            throw new IllegalArgumentException("Médico com CRM " + doctor.getCrm() + " não encontrado.");
        }
        if (!patientRepository.existsById(patient.getCpf())) {
            throw new IllegalArgumentException("Paciente com CPF " + patient.getCpf() + " não encontrado.");
        }

        // Criação da consulta
        Consultation consultation = new Consultation();
        consultation.setConsultationId(UUID.randomUUID());
        consultation.setDate(date);
        consultation.setPatient(patient);
        consultation.setDoctor(doctor);
        consultation.setUrgent(isUrgent);
        consultation.setObservation(observation);
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



}
