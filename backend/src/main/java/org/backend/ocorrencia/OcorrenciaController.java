package org.backend.ocorrencia;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.PrioridadeOcorrencia;
import org.backend.enums.StatusOcorrencia;
import org.backend.exception.RegraNegocioInvalidaException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
@CrossOrigin
@SecurityRequirement(name = "BearerAuth")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @GetMapping()
    public ResponseEntity<List<OcorrenciaDTO>> get() {
        List<Ocorrencia> ocorrencias = ocorrenciaService.getOcorrencias();
        return ResponseEntity.ok(
                ocorrencias.stream().map(OcorrenciaDTO::create).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> get(@PathVariable("id") Long id) {
        Optional<Ocorrencia> ocorrencia = ocorrenciaService.getOcorrenciaById(id);
        if (!ocorrencia.isPresent()) {
            return new ResponseEntity("Ocorrência não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(OcorrenciaDTO.create(ocorrencia.get()));
    }

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody OcorrenciaDTO dto) {
        try {
            Ocorrencia ocorrencia = converter(dto);
            ocorrencia = ocorrenciaService.save(ocorrencia);
            return new ResponseEntity(OcorrenciaDTO.create(ocorrencia), HttpStatus.CREATED);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @Valid @RequestBody OcorrenciaDTO dto) {
        if (!ocorrenciaService.getOcorrenciaById(id).isPresent()) {
            return new ResponseEntity("Ocorrência não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Ocorrencia ocorrencia = converter(dto);
            ocorrencia.setId(id);
            ocorrencia = ocorrenciaService.save(ocorrencia);
            return ResponseEntity.ok(OcorrenciaDTO.create(ocorrencia));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Ocorrencia> ocorrencia = ocorrenciaService.getOcorrenciaById(id);
        if (!ocorrencia.isPresent()) {
            return new ResponseEntity("Ocorrência não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ocorrenciaService.delete(ocorrencia.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Ocorrencia converter(OcorrenciaDTO dto) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        Point point = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );

        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(dto.getId());
        ocorrencia.setTipoOcorrencia(dto.getTipoOcorrencia());
        ocorrencia.setDescricaoOcorrencia(dto.getDescricaoOcorrencia());
        ocorrencia.setDataHora(dto.getData());
        ocorrencia.setStatusOcorrencia(StatusOcorrencia.ABERTO);
        ocorrencia.setPrioridadeOcorrencia(PrioridadeOcorrencia.MEDIA);
        ocorrencia.setLocalizacao(point);

        return ocorrencia;
    }

}