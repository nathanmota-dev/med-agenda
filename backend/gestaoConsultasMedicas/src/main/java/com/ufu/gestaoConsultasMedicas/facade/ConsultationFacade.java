package com.ufu.gestaoConsultasMedicas.facade;

import com.ufu.gestaoConsultasMedicas.decorator.ConsultationService;
import com.ufu.gestaoConsultasMedicas.decorator.ConsultationServiceImpl;
import com.ufu.gestaoConsultasMedicas.decorator.UrgentConsultationDecorator;
import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ConsultationFacade {

    private final ConsultationService consultationService;

    @Autowired
    public ConsultationFacade(ConsultationServiceImpl consultationServiceImpl) {
        this.consultationService = consultationServiceImpl;
    }

    public Consultation createConsultation(Patient patient, Doctor doctor, LocalDate date, boolean isUrgent, String observation) {
        ConsultationService service = consultationService;

        if (isUrgent) {
            // Adiciona o decorator caso seja uma consulta urgente
            service = new UrgentConsultationDecorator(service);
        }

        return service.createConsultation(patient, doctor, date, observation);
    }
}
