package com.ufu.gestaoConsultasMedicas.decorator;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Autowired
    public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDateTime dateTime, String observation) {
        Consultation consultation = new Consultation(UUID.randomUUID(), dateTime, patient, doctor, false);
        consultation.setObservation(observation);
        return consultationRepository.save(consultation);
    }
}
