package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("findByEmail deve retornar médico quando email existir")
    void findByEmail_whenExists() {
        Doctor doctor = new Doctor("123456789", "house@hospital.com", "senha123", "Dr. House", "Cardiologia", "3499999999", BigDecimal.valueOf(200.0));
        em.persistAndFlush(doctor);

        Optional<Doctor> found = doctorRepository.findByEmail("house@hospital.com");

        assertThat(found).isPresent();
        assertThat(found.get().getCrm()).isEqualTo("123456789");
    }

    @Test
    @DisplayName("findByEmail deve retornar Optional.empty quando email não existir")
    void findByEmail_whenNotExists() {
        Optional<Doctor> found = doctorRepository.findByEmail("naoexiste@acme.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findConsultationDateTimesByCrm deve retornar as datas das consultas do médico")
    void findConsultationDateTimesByCrm_returnsDates() {
        Patient patient = new Patient("12345678901", "patient@acme.com", "secret",
                "Paciente Teste", LocalDate.of(1990,1,1), "Rua 1", "Histórico ok");
        em.persist(patient);

        Doctor doc = new Doctor("222333444", "who@hospital.com", "pwd", "Dr. Who", "Cardio", "34988887777", BigDecimal.valueOf(200.0));
        em.persist(doc);

        LocalDateTime d1 = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime d2 = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0).withSecond(0).withNano(0);

        Consultation c1 = new Consultation();
        c1.setDateTime(d1);
        c1.setPatient(patient);
        c1.setDoctor(doc);
        c1.setUrgent(false);
        em.persist(c1);

        Consultation c2 = new Consultation();
        c2.setDateTime(d2);
        c2.setPatient(patient);
        c2.setDoctor(doc);
        c2.setUrgent(true);
        em.persist(c2);

        em.flush();

        List<LocalDateTime> dates = doctorRepository.findConsultationDateTimesByCrm("222333444");
        assertThat(dates).containsExactlyInAnyOrder(d1, d2);
    }

    @Test
    @DisplayName("Deve falhar ao salvar dois médicos com o mesmo email (unicidade)")
    void uniqueEmail_constraint() {
        Doctor d1 = new Doctor("000111222", "unique@hospital.com", "pwd", "Dr. A", "Orto", "34911110000", BigDecimal.valueOf(200.0));
        doctorRepository.saveAndFlush(d1);

        Doctor d2 = new Doctor("000111223", "unique@hospital.com", "pwd", "Dr. B", "Orto", "34911110001", BigDecimal.valueOf(200.0));

        assertThatThrownBy(() -> {
            doctorRepository.save(d2);
            doctorRepository.flush();
        }).isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}
