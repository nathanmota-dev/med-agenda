package com.ufu.gestaoConsultasMedicas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID consultationId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    public Consultation(UUID consultationId, LocalDate date, Patient patient, Doctor doctor, boolean isUrgent) {
        this.consultationId = consultationId;
        this.date = date;
        this.patient = patient;
        this.doctor = doctor;
        this.isUrgent = isUrgent;
    }

    public UUID getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(UUID consultationId) {
        this.consultationId = consultationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
