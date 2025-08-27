package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.*;

@Entity
@Table(
        name = "habilidades_medicas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"nome"})
)
public class HabilidadeMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 100)
    private String tipo;

    public HabilidadeMedica() {}

    public HabilidadeMedica(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}