package org.backend.arearisco;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.locationtech.jts.geom.Point;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaRisco {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelRisco nivelRisco;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point localizacao;
}