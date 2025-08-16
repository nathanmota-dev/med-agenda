package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByCpf deve retornar paciente quando CPF existir")
    void findByCpf_found() {
        Patient patient = new Patient(
                "12345678901",
                "paciente@teste.com",
                "senha123",
                "João da Silva",
                LocalDate.of(1990, 1, 1),
                "Rua A, 123",
                "Sem histórico"
        );
        entityManager.persist(patient);
        entityManager.flush();

        Optional<Patient> found = patientRepository.findByCpf("12345678901");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("João da Silva");
    }

    @Test
    @DisplayName("findByCpf deve retornar vazio quando CPF não existir")
    void findByCpf_notFound() {
        Optional<Patient> found = patientRepository.findByCpf("00000000000");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findByEmail deve retornar paciente quando email existir")
    void findByEmail_found() {
        Patient patient = new Patient(
                "98765432100",
                "email@teste.com",
                "senha123",
                "Maria Oliveira",
                LocalDate.of(1985, 5, 10),
                "Rua B, 456",
                "Diabetes"
        );
        entityManager.persist(patient);
        entityManager.flush();

        Optional<Patient> found = patientRepository.findByEmail("email@teste.com");

        assertThat(found).isPresent();
        assertThat(found.get().getCpf()).isEqualTo("98765432100");
    }

    @Test
    @DisplayName("findByEmail deve retornar vazio quando email não existir")
    void findByEmail_notFound() {
        Optional<Patient> found = patientRepository.findByEmail("naoexiste@teste.com");
        assertThat(found).isEmpty();
    }
}
