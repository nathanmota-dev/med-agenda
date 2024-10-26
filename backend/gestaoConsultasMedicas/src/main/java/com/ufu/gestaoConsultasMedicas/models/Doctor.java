package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @Column(name = "crm", length = 9, nullable = false, unique = true)
    private String crm;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    public Doctor(){
    }

    public Doctor(String crm, String name, String specialty, String telephone) {
        this.crm = crm;
        this.name = name;
        this.specialty = specialty;
        this.telephone = telephone;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
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
}
