package com.ufu.gestaoConsultasMedicas.service;
import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Diagnosis;
import com.ufu.gestaoConsultasMedicas.repository.ConsultationRepository;
import com.ufu.gestaoConsultasMedicas.repository.DiagnosisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    public Optional<Diagnosis> createDiagnosis(Diagnosis diagnosis) {
        UUID consultaId = diagnosis.getConsulta().getConsultationId();
        Optional<Consultation> consulta = consultationRepository.findByConsultationId(consultaId);

        if (consulta.isPresent()) {
            diagnosis.setConsulta(consulta.get());
            return Optional.of(diagnosisRepository.save(diagnosis));
        }

        return Optional.empty();
    }

}