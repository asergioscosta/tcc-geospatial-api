package org.backend.arearisco;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.backend.enums.StatusAreaRisco;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaRiscoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private NivelRisco nivelRisco;
    private StatusAreaRisco statusAreaRisco;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public static AreaRiscoDTO create(AreaRisco areaRisco) {
        AreaRiscoDTO dto = new AreaRiscoDTO();

        dto.setId(areaRisco.getId());
        dto.setNome(areaRisco.getNome());
        dto.setDescricao(areaRisco.getDescricao());
        dto.setNivelRisco(areaRisco.getNivelRisco());
        dto.setLatitude(areaRisco.getLatitude());
        dto.setLongitude(areaRisco.getLongitude());
        dto.setStatusAreaRisco(areaRisco.getStatusAreaRisco());

        return dto;
    }
}