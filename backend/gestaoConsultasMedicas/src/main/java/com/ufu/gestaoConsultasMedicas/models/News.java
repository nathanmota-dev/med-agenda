package com.ufu.gestaoConsultasMedicas.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "noticias", uniqueConstraints = @UniqueConstraint(columnNames = "link"))
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String titulo;

    @Column(nullable = false, unique = true, length = 1000)
    private String link;

    @Column(nullable = false)
    private OffsetDateTime data;   // data de coleta
}
