package org.backend.pontocritico;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.backend.enums.StatusPontoCritico;
import org.backend.enums.TipoPontoCritico;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoCriticoDTO {

    private Long id;
    private String nomePontoCritico;
    private String descricaoPontoCritico;
    private TipoPontoCritico tipoPontoCritico;
    private StatusPontoCritico statusPontoCritico;
    private NivelRisco nivelRisco;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public static PontoCriticoDTO create(PontoCritico pontoCritico) {
        PontoCriticoDTO dto = new PontoCriticoDTO();

        dto.setId(pontoCritico.getId());
        dto.setNomePontoCritico(pontoCritico.getNomePontoCritico());
        dto.setDescricaoPontoCritico(pontoCritico.getDescricaoPontoCritico());
        dto.setTipoPontoCritico(pontoCritico.getTipoPontoCritico());
        dto.setStatusPontoCritico(pontoCritico.getStatusPontoCritico());
        dto.setNivelRisco(pontoCritico.getNivelRisco());
        dto.setLatitude(pontoCritico.getLatitude());
        dto.setLongitude(pontoCritico.getLongitude());

        return dto;
    }
}
