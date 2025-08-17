package com.ufu.gestaoConsultasMedicas.models;

public enum PaymentStatus {
    PENDING,   // Pagamento gerado, aguardando confirmação
    PAID,      // Pago com sucesso
    FAILED,    // Falhou (ex: cartão recusado)
    REFUNDED,  // Estornado
    CANCELLED  // Cancelado antes de ser pago
}
