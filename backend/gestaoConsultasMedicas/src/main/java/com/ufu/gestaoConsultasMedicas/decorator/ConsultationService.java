package com.ufu.gestaoConsultasMedicas.decorator;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ConsultationService {
    Consultation createConsultation(Patient patient, Doctor doctor, LocalDateTime dateTime, String observation);
}
