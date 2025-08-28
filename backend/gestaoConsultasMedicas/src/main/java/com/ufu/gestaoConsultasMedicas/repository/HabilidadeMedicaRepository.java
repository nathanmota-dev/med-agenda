package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.HabilidadeMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HabilidadeMedicaRepository extends JpaRepository<HabilidadeMedica, Long> {
    Optional<HabilidadeMedica> findByNomeIgnoreCase(String nome);
}