package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @Column(name = "crm", length = 9, nullable = false, unique = true)
    private String crm;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "consultation_value", nullable = false)
    private BigDecimal consultationValue;

    public Doctor(){
    }

    public Doctor(String crm, String email, String password, String name, String specialty, String telephone, BigDecimal consultationValue) {
        this.crm = crm;
        this.email = email;
        this.password = password;
        this.name = name;
        this.specialty = specialty;
        this.telephone = telephone;
        this.consultationValue = consultationValue;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getConsultationValue() {
        return consultationValue;
    }

    public void setConsultationValue(BigDecimal consultationValue) {
        this.consultationValue = consultationValue;
    }
}
