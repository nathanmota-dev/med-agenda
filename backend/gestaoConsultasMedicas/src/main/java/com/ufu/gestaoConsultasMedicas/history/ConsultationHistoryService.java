package com.ufu.gestaoConsultasMedicas.history;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultationHistoryService implements ConsultationHistoryCreation, ConsultationHistoryQuery {

    private final ConsultationRepository consultationRepository;

    @Autowired
    public ConsultationHistoryService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public void addConsultationToHistory(UUID patientId, UUID consultationId) {
        System.out.println("Consulta adicionada ao histórico do paciente com ID " + patientId);
    }

    @Override
    public List<Consultation> getPatientConsultationHistory(UUID patientId) {
        return consultationRepository.findByPatient_Cpf(patientId.toString());
    }

    @Override
    public List<Consultation> getDoctorConsultationHistory(UUID doctorId) {
        return consultationRepository.findByDoctor_Crm(doctorId.toString());
    }
}
