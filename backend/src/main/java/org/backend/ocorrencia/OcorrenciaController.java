package org.backend.ocorrencia;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.PrioridadeOcorrencia;
import org.backend.enums.StatusOcorrencia;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
@CrossOrigin
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @GetMapping
    public ResponseEntity<List<OcorrenciaDTO>> get() {
        List<Ocorrencia> ocorrencias = ocorrenciaService.getOcorrencias();

        return ResponseEntity.ok(
                ocorrencias.stream()
                        .map(OcorrenciaDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Ocorrencia> ocorrencia = ocorrenciaService.getOcorrenciaById(id);

        if (ocorrencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ocorrência não encontrada");
        }

        return ResponseEntity.ok(OcorrenciaDTO.create(ocorrencia.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody OcorrenciaDTO dto) {
        try {
            Ocorrencia ocorrencia = converter(dto, true, null);
            ocorrencia = ocorrenciaService.save(ocorrencia);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(OcorrenciaDTO.create(ocorrencia));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody OcorrenciaDTO dto) {
        Optional<Ocorrencia> existente = ocorrenciaService.getOcorrenciaById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ocorrência não encontrada");
        }

        try {
            Ocorrencia ocorrencia = converter(dto, false, existente.get());
            ocorrencia.setId(id);
            ocorrencia = ocorrenciaService.save(ocorrencia);

            return ResponseEntity.ok(OcorrenciaDTO.create(ocorrencia));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Ocorrencia> ocorrencia = ocorrenciaService.getOcorrenciaById(id);

        if (ocorrencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ocorrência não encontrada");
        }

        ocorrenciaService.delete(ocorrencia.get());
        return ResponseEntity.noContent().build();
    }

    private Ocorrencia converter(OcorrenciaDTO dto, boolean cadastroPublico, Ocorrencia existente) {
        Ocorrencia ocorrencia = new Ocorrencia();

        ocorrencia.setId(dto.getId());
        ocorrencia.setTipoOcorrencia(dto.getTipoOcorrencia());
        ocorrencia.setDescricaoOcorrencia(dto.getDescricaoOcorrencia());
        ocorrencia.setNivelRisco(dto.getNivelRisco());
        ocorrencia.setLatitude(dto.getLatitude());
        ocorrencia.setLongitude(dto.getLongitude());

        if (dto.getData() != null) {
            ocorrencia.setDataHora(dto.getData());
        } else if (existente != null && existente.getDataHora() != null) {
            ocorrencia.setDataHora(existente.getDataHora());
        } else {
            ocorrencia.setDataHora(LocalDateTime.now());
        }

        if (cadastroPublico) {
            ocorrencia.setStatusOcorrencia(StatusOcorrencia.ABERTO);
            ocorrencia.setPrioridadeOcorrencia(PrioridadeOcorrencia.MEDIA);
        } else {
            ocorrencia.setStatusOcorrencia(
                    dto.getStatusOcorrencia() != null
                            ? dto.getStatusOcorrencia()
                            : existente.getStatusOcorrencia()
            );

            ocorrencia.setPrioridadeOcorrencia(
                    dto.getPrioridadeOcorrencia() != null
                            ? dto.getPrioridadeOcorrencia()
                            : existente.getPrioridadeOcorrencia()
            );
        }

        return ocorrencia;
    }
}
