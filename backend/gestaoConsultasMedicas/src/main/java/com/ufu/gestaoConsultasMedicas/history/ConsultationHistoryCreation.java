package com.ufu.gestaoConsultasMedicas.history;

import java.util.UUID;

public interface ConsultationHistoryCreation {
    void addConsultationToHistory(UUID patientId, UUID consultationId);
}
