package org.backend.arearisco;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.backend.enums.StatusAreaRisco;

@Entity
@Table(name = "area_risco")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaRisco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelRisco nivelRisco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAreaRisco statusAreaRisco;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}