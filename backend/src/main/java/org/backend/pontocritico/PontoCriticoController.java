package org.backend.pontocritico;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.StatusPontoCritico;
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
@RequestMapping("/ponto-critico")
@RequiredArgsConstructor
@CrossOrigin
@SecurityRequirement(name = "BearerAuth")
public class PontoCriticoController {

    private final PontoCriticoService pontoCriticoService;

    @GetMapping()
    public ResponseEntity<List<PontoCriticoDTO>> get() {
        List<PontoCritico> ocorrencias = pontoCriticoService.getPontosCriticos();
        return ResponseEntity.ok(
                ocorrencias.stream().map(PontoCriticoDTO::create).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoCriticoDTO> get(@PathVariable("id") Long id) {
        Optional<PontoCritico> pontoCritico = pontoCriticoService.getPontoCriticoById(id);
        if (!pontoCritico.isPresent()) {
            return new ResponseEntity("Ponto Crítico não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(PontoCriticoDTO.create(pontoCritico.get()));
    }

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody PontoCriticoDTO dto) {
        try {
            PontoCritico pontoCritico = converter(dto);
            pontoCritico = pontoCriticoService.save(pontoCritico);
            return new ResponseEntity(PontoCriticoDTO.create(pontoCritico), HttpStatus.CREATED);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @Valid @RequestBody PontoCriticoDTO dto) {
        if (!pontoCriticoService.getPontoCriticoById(id).isPresent()) {
            return new ResponseEntity("Ponto Crítico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PontoCritico pontoCritico = converter(dto);
            pontoCritico.setId(id);
            pontoCritico = pontoCriticoService.save(pontoCritico);
            return ResponseEntity.ok(PontoCriticoDTO.create(pontoCritico));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<PontoCritico> pontoCritico = pontoCriticoService.getPontoCriticoById(id);
        if (!pontoCritico.isPresent()) {
            return new ResponseEntity("Ponto Crítico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            pontoCriticoService.delete(pontoCritico.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public PontoCritico converter(PontoCriticoDTO dto) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        Point point = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );

        PontoCritico pontoCritico = new PontoCritico();
        pontoCritico.setId(dto.getId());
        pontoCritico.setNomePontoCritico(dto.getNomePontoCritico());
        pontoCritico.setDescricaoPontoCritico(dto.getDescricaoPontoCritico());
        pontoCritico.setTipoPontoCritico(dto.getTipoPontoCritico());
        pontoCritico.setStatusPontoCritico(StatusPontoCritico.ATIVO);
        pontoCritico.setLocalizacao(point);

        return pontoCritico;
    }

}