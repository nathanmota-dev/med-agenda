package com.ufu.gestaoConsultasMedicas.dto;

import java.time.OffsetDateTime;

public record NewsDTO(Long id, String titulo, String link, OffsetDateTime data) {}
