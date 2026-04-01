package org.backend.ocorrencia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.PrioridadeOcorrencia;
import org.backend.enums.StatusOcorrencia;
import org.backend.enums.TipoOcorrencia;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOcorrencia tipoOcorrencia;

    private String descricaoOcorrencia;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOcorrencia statusOcorrencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeOcorrencia prioridadeOcorrencia;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point localizacao;
}