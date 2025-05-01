package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    Optional<Consultation> findByConsultationId(UUID consultationId);
    List<Consultation> findByDoctor_Crm(String doctorId);
    List<Consultation> findByPatient_Cpf(String patientCpf);


    @Query("""
      SELECT c
      FROM Consultation c
      WHERE c.dateTime BETWEEN :startOfDay AND :endOfDay
    """)
    List<Consultation> findByDateTimeBetween(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay")   LocalDateTime endOfDay
    );

    default List<Consultation> findByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay().minusNanos(1);
        return findByDateTimeBetween(start, end);
    }
}
