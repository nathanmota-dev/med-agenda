package com.ufu.gestaoConsultasMedicas.decorator;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;

import java.time.LocalDate;

public class UrgentConsultationDecorator implements ConsultationService {

    private final ConsultationService decoratedService;

    public UrgentConsultationDecorator(ConsultationService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override
    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDate date, String observation) {
        // Criação de uma consulta urgente
        Consultation consultation = decoratedService.createConsultation(patient, doctor, date, observation);
        consultation.setUrgent(true);
        return consultation;
    }
}
