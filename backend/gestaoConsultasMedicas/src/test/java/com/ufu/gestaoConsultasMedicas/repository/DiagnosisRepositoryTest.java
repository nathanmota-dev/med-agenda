package com.ufu.gestaoConsultasMedicas.repository;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Diagnosis;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DiagnosisRepositoryTest {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private TestEntityManager em;

    private Doctor persistDoctor(String crm) {
        Doctor d = new Doctor(
                crm,
                "doctor"+crm+"@hospital.com",
                "pwd",
                "Dr. "+crm,
                "Cardio",
                "3499999999"
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

    private Consultation persistConsultation(Doctor d, Patient p, LocalDateTime when, boolean urgent, String obs) {
        Consultation c = new Consultation();
        c.setDateTime(when);
        c.setDoctor(d);
        c.setPatient(p);
        c.setUrgent(urgent);
        c.setObservation(obs);
        return em.persistAndFlush(c);
    }

    private Diagnosis persistDiagnosis(Consultation c, String descricao, String cid, LocalDate data) {
        Diagnosis dx = new Diagnosis();
        dx.setConsulta(c);
        dx.setDescricao(descricao);
        dx.setCid(cid);
        dx.setData(data);
        return em.persistAndFlush(dx);
    }

    @Test
    @DisplayName("findByConsulta_ConsultationId deve retornar o diagnóstico da consulta")
    void findByConsulta_ConsultationId_found() {
        // arrange
        Doctor doc = persistDoctor("111222333");
        Patient pat = persistPatient("12345678901");
        Consultation cons = persistConsultation(doc, pat, LocalDateTime.of(2025, 1, 1, 14, 0), false, "Rotina");
        Diagnosis saved = persistDiagnosis(cons, "Gripe comum", "J11.1", LocalDate.of(2025, 1, 1));

        // act
        Optional<Diagnosis> found = diagnosisRepository.findByConsulta_ConsultationId(cons.getConsultationId());

        // assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getDescricao()).isEqualTo("Gripe comum");
        assertThat(found.get().getCid()).isEqualTo("J11.1");
        assertThat(found.get().getConsulta().getConsultationId()).isEqualTo(cons.getConsultationId());
    }

    @Test
    @DisplayName("findByConsulta_ConsultationId deve retornar vazio quando não houver diagnóstico")
    void findByConsulta_ConsultationId_notFound() {
        Optional<Diagnosis> notFound = diagnosisRepository.findByConsulta_ConsultationId(
                java.util.UUID.randomUUID()
        );
        assertThat(notFound).isEmpty();
    }
}
