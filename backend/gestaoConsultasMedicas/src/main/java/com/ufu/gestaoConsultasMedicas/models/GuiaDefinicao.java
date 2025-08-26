package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.*;

@Entity
@Table(
        name = "guia_definicoes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"termo"})
)
public class GuiaDefinicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String termo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String definicao;

    public GuiaDefinicao() {}

    public GuiaDefinicao(String termo, String definicao) {
        this.termo = termo;
        this.definicao = definicao;
    }

    public Long getId() { return id; }
    public String getTermo() { return termo; }
    public String getDefinicao() { return definicao; }
    public void setId(Long id) { this.id = id; }
    public void setTermo(String termo) { this.termo = termo; }
    public void setDefinicao(String definicao) { this.definicao = definicao; }
}
