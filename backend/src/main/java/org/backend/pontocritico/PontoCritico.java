package org.backend.pontocritico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.backend.enums.StatusPontoCritico;
import org.backend.enums.TipoPontoCritico;

@Entity
@Table(name = "ponto_critico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontoCritico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomePontoCritico;

    @Column(nullable = false)
    private String descricaoPontoCritico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPontoCritico tipoPontoCritico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPontoCritico statusPontoCritico;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelRisco nivelRisco;
}