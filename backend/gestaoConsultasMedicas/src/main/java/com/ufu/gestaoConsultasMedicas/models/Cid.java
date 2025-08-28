package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "cid")
public class Cid {

    @Id
    @Column(name = "code", length = 5, nullable = false, unique = true)
    private String code; // Exemplos: A00, A15.3

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public Cid() {}

    public Cid(String code) {
        this.code = code;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
