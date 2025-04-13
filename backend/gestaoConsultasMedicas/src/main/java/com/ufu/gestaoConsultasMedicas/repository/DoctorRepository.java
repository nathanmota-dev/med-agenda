package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByCrm(String crm);
    Optional<Doctor> findByEmail(String email);

    @Query("SELECT c.date FROM Consultation c WHERE c.doctor.crm = :crm")
    List<LocalDate> findConsultationDatesByCrm(@Param("crm") String crm);
}