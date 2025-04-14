package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consultation consulta;

    @JoinColumn(name = "cid_id", nullable = false)
    private String cid;

    public Diagnosis(){
    }

    public Diagnosis(UUID id, String descricao, LocalDate data, Consultation consulta, String cid) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.consulta = consulta;
        this.cid = cid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Consultation getConsulta() {
        return consulta;
    }

    public void setConsulta(Consultation consulta) {
        this.consulta = consulta;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}