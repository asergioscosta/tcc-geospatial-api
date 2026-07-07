package org.backend.ocorrencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.NivelRisco;
import org.backend.enums.PrioridadeOcorrencia;
import org.backend.enums.StatusOcorrencia;
import org.backend.enums.TipoOcorrencia;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaDTO {

    private Long id;
    private TipoOcorrencia tipoOcorrencia;
    private String descricaoOcorrencia;
    private LocalDateTime data;
    private StatusOcorrencia statusOcorrencia;
    private PrioridadeOcorrencia prioridadeOcorrencia;
    private NivelRisco nivelRisco;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public static OcorrenciaDTO create(Ocorrencia ocorrencia) {
        OcorrenciaDTO dto = new OcorrenciaDTO();

        dto.setId(ocorrencia.getId());
        dto.setTipoOcorrencia(ocorrencia.getTipoOcorrencia());
        dto.setDescricaoOcorrencia(ocorrencia.getDescricaoOcorrencia());
        dto.setData(ocorrencia.getDataHora());
        dto.setStatusOcorrencia(ocorrencia.getStatusOcorrencia());
        dto.setPrioridadeOcorrencia(ocorrencia.getPrioridadeOcorrencia());
        dto.setNivelRisco(ocorrencia.getNivelRisco());
        dto.setLatitude(ocorrencia.getLatitude());
        dto.setLongitude(ocorrencia.getLongitude());

        return dto;
    }
}
