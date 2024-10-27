package com.ufu.gestaoConsultasMedicas.history;

import com.ufu.gestaoConsultasMedicas.models.Consultation;

import java.util.List;
import java.util.UUID;

public interface ConsultationHistoryQuery {
    List<Consultation> getPatientConsultationHistory(UUID patientId);
    List<Consultation> getDoctorConsultationHistory(UUID doctorId);
}
