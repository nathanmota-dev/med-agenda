package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    Optional<Consultation> findByConsultationId(UUID consultationId);
    List<Consultation> findByDoctor_Crm(String doctorId);
    List<Consultation> findByPatient_Cpf(String patientCpf);
    List<Consultation> findByDate(LocalDate date);
}
