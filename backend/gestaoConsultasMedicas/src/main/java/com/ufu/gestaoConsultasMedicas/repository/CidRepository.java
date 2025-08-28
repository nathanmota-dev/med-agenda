package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Cid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidRepository extends JpaRepository<Cid, String> {
    Optional<Cid> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}
