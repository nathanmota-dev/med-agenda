package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.*;
import com.ufu.gestaoConsultasMedicas.repository.ConsultationRepository;
import com.ufu.gestaoConsultasMedicas.repository.DoctorRepository;
import com.ufu.gestaoConsultasMedicas.repository.PatientRepository;
import com.ufu.gestaoConsultasMedicas.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository,
                               DoctorRepository doctorRepository,
                               PatientRepository patientRepository,
                               EmailService emailService,
                               PaymentRepository paymentRepository) {
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
        this.paymentRepository = paymentRepository;
    }

    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDateTime dateTime, boolean isUrgent, String observation) {
        Patient patientFromDb = patientRepository.findByCpf(patient.getCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente com CPF " + patient.getCpf() + " não encontrado."));

        Doctor doctorFromDb = doctorRepository.findByCrm(doctor.getCrm())
                .orElseThrow(() -> new IllegalArgumentException("Médico com CRM " + doctor.getCrm() + " não encontrado."));

        // Criação da consulta
        Consultation consultation = new Consultation();
        consultation.setConsultationId(UUID.randomUUID());
        consultation.setDateTime(dateTime);
        consultation.setDuracaoMinutos(60);
        consultation.setPatient(patientFromDb);
        consultation.setDoctor(doctorFromDb);
        consultation.setUrgent(isUrgent);
        consultation.setObservation(observation);

        consultation = consultationRepository.save(consultation);

        // Criação do pagamento vinculado
        Payment payment = new Payment();
        payment.setConsultation(consultation);
        payment.setAmount(doctorFromDb.getConsultationValue());
        payment.setStatus(PaymentStatus.PENDING); // valor inicial
        paymentRepository.save(payment);

        // Envio de e-mail
        emailService.sendEmail(
                "delivered@resend.dev",
                "Consulta confirmada",
                "Sua consulta foi marcada para " + consultation.getDateTime()
        );

        return consultation;
    }

    public Optional<Consultation> updateConsultation(UUID consultationId, LocalDateTime dateTime, String observation) {
        return consultationRepository.findById(consultationId).map(consultation -> {
            consultation.setDateTime(dateTime);
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