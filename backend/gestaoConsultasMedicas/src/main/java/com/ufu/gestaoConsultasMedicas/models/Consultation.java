package com.ufu.gestaoConsultasMedicas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID consultationId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "duracao_minutos", nullable = false)
    private int duracaoMinutos = 60;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "cpf", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "crm", nullable = false)
    private Doctor doctor;

    @Column(name = "is_urgent", nullable = false)
    private boolean isUrgent;

    @Column(name = "observation")
    private String observation;

    public Consultation() {
    }

    public Consultation(UUID consultationId, LocalDateTime dateTime, Patient patient, Doctor doctor, boolean isUrgent) {
        this.consultationId = consultationId;
        this.dateTime = dateTime;
        this.patient = patient;
        this.doctor = doctor;
        this.isUrgent = isUrgent;
        this.duracaoMinutos = 60;
    }

    public UUID getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(UUID consultationId) {
        this.consultationId = consultationId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @JsonProperty("isUrgent")
    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
