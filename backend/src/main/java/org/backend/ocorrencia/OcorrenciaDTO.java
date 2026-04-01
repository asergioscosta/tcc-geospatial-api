package org.backend.ocorrencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private LocalDateTime dataHora;
    private StatusOcorrencia statusOcorrencia;
    private PrioridadeOcorrencia prioridadeOcorrencia;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public static OcorrenciaDTO create(Ocorrencia ocorrencia) {

        OcorrenciaDTO dto = new OcorrenciaDTO();

        dto.setId(ocorrencia.getId());
        dto.setTipoOcorrencia(ocorrencia.getTipoOcorrencia());
        dto.setDescricaoOcorrencia(ocorrencia.getDescricaoOcorrencia());
        dto.setDataHora(ocorrencia.getDataHora());
        dto.setStatusOcorrencia(ocorrencia.getStatusOcorrencia());
        dto.setPrioridadeOcorrencia(ocorrencia.getPrioridadeOcorrencia());

        if (ocorrencia.getLocalizacao() != null) {
            dto.setLatitude(ocorrencia.getLocalizacao().getY());
            dto.setLongitude(ocorrencia.getLocalizacao().getX());
        }

        return dto;
    }
}