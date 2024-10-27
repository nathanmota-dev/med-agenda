package com.ufu.gestaoConsultasMedicas.decorator;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;

import java.time.LocalDate;

public interface ConsultationService {
    Consultation createConsultation(Patient patient, Doctor doctor, LocalDate date, String observation);
}
