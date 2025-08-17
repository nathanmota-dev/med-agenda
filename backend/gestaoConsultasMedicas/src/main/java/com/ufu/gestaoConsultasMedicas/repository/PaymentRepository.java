package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByConsultation_ConsultationId(UUID consultationId);
    Optional<Payment> findByTransactionReference(String transactionReference);
}
