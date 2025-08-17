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

@DataJpaTest
@ActiveProfiles("test")
class ConsultationRepositoryTest {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private TestEntityManager em;

    private Doctor persistDoctor(String crm) {
        Doctor d = new Doctor(
                crm,
                "doctor"+crm+"@hospital.com",
                "pwd",
                "Dr. Test "+crm,
                "Cardio",
                "3499999999",
                BigDecimal.valueOf(200.0)
        );
        return em.persistAndFlush(d);
    }

    private Patient persistPatient(String cpf) {
        Patient p = new Patient(
                cpf,
                "p"+cpf+"@test.com",
                "pwd",
                "Paciente "+cpf,
                LocalDate.of(1990, 1, 1),
                "Rua A, 123",
                "Sem histórico"
        );
        return em.persistAndFlush(p);
    }

    private Consultation persistConsultation(Doctor d, Patient p, LocalDateTime when, boolean urgent, String obs, Integer duracaoMin) {
        Consultation c = new Consultation();
        c.setDateTime(when);
        if (duracaoMin != null) c.setDuracaoMinutos(duracaoMin);
        c.setDoctor(d);
        c.setPatient(p);
        c.setUrgent(urgent);
        c.setObservation(obs);
        return em.persistAndFlush(c);
    }

    @Test
    @DisplayName("save/find: deve salvar e buscar por consultationId (findByConsultationId)")
    void saveAndFindByConsultationId() {
        Doctor d = persistDoctor("111222333");
        Patient p = persistPatient("12345678901");

        Consultation saved = persistConsultation(d, p, LocalDateTime.of(2025,1,1,14,0), false, "Rotina", 30);

        Optional<Consultation> found = consultationRepository.findByConsultationId(saved.getConsultationId());

        assertThat(found).isPresent();
        assertThat(found.get().getObservation()).isEqualTo("Rotina");
        assertThat(found.get().getDoctor().getCrm()).isEqualTo("111222333");
        assertThat(found.get().getPatient().getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("findByDoctor_Crm: deve retornar todas as consultas do CRM informado")
    void findByDoctorCrm() {
        Doctor d = persistDoctor("222333444");
        Patient p = persistPatient("98765432100");

        persistConsultation(d, p, LocalDateTime.now().withSecond(0).withNano(0), false, "Checkup", 30);
        persistConsultation(d, p, LocalDateTime.now().plusDays(1).withSecond(0).withNano(0), true, "Urgente", 45);

        List<Consultation> list = consultationRepository.findByDoctor_Crm("222333444");

        assertThat(list).hasSize(2);
        assertThat(list).allMatch(c -> "222333444".equals(c.getDoctor().getCrm()));
    }

    @Test
    @DisplayName("findByPatient_Cpf: deve retornar todas as consultas do CPF informado")
    void findByPatientCpf() {
        Doctor d = persistDoctor("333444555");
        Patient p = persistPatient("11122233344");

        persistConsultation(d, p, LocalDateTime.now().withSecond(0).withNano(0), false, "Normal", null);

        List<Consultation> list = consultationRepository.findByPatient_Cpf("11122233344");

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getPatient().getCpf()).isEqualTo("11122233344");
    }

    @Test
    @DisplayName("findByDate: deve retornar consultas apenas do dia informado")
    void findByDate_onlyThatDay() {
        Doctor d = persistDoctor("444555666");
        Patient p = persistPatient("22233344455");

        LocalDate today = LocalDate.of(2025, 1, 10);
        persistConsultation(d, p, today.atTime(9, 0), false, "Manhã", null);
        persistConsultation(d, p, today.atTime(15, 30), true, "Tarde", null);
        persistConsultation(d, p, today.plusDays(1).atTime(10, 0), false, "Outro dia", null);

        List<Consultation> onlyToday = consultationRepository.findByDate(today);

        assertThat(onlyToday).hasSize(2);
        assertThat(onlyToday).allMatch(c -> c.getDateTime().toLocalDate().equals(today));
    }
}
