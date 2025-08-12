package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("deve encontrar Admin por email quando existir")
    void testFindByEmail_whenExists() {

        Admin admin = new Admin();
        admin.setName("Alice Admin");
        admin.setEmail("alice@example.com");
        admin.setPassword("plain-secret");
        em.persistAndFlush(admin);

        Optional<Admin> found = adminRepository.findByEmail("alice@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alice@example.com");
        assertThat(found.get().getUserId()).isNotNull();
    }

    @Test
    @DisplayName("deve retornar Optional.empty quando email não existir")
    void testFindByEmail_whenNotExists() {
        Optional<Admin> found = adminRepository.findByEmail("naoexiste@example.com");
        assertThat(found).isEmpty();
    }
}
