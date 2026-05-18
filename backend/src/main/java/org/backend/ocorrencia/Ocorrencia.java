package org.backend.ocorrencia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.PrioridadeOcorrencia;
import org.backend.enums.StatusOcorrencia;
import org.backend.enums.TipoOcorrencia;

import java.time.LocalDateTime;

@Entity
@Table(name = "ocorrencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOcorrencia tipoOcorrencia;

    @Column(nullable = false)
    private String descricaoOcorrencia;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOcorrencia statusOcorrencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeOcorrencia prioridadeOcorrencia;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}