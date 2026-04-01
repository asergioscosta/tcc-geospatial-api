package org.backend.pontocritico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.StatusPontoCritico;
import org.backend.enums.TipoPontoCritico;
import org.locationtech.jts.geom.Point;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoCritico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePontoCritico;
    private String descricaoPontoCritico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPontoCritico tipoPontoCritico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPontoCritico statusPontoCritico;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point localizacao;
}