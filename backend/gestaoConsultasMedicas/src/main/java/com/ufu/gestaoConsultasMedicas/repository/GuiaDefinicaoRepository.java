package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.GuiaDefinicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuiaDefinicaoRepository extends JpaRepository<GuiaDefinicao, Long> {
    Optional<GuiaDefinicao> findByTermoIgnoreCase(String termo);
}
