package org.backend.arearisco;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/area-risco")
@RequiredArgsConstructor
@CrossOrigin
@SecurityRequirement(name = "BearerAuth")
public class AreaRiscoController {

    private final AreaRiscoService areaRiscoService;

    @GetMapping()
    public ResponseEntity<List<AreaRiscoDTO>> get() {
        List<AreaRisco> areasRisco = areaRiscoService.getAreasRisco();
        return ResponseEntity.ok(areasRisco.stream().map(AreaRiscoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaRiscoDTO> get(@PathVariable("id") Long id) {
        Optional<AreaRisco> areaRisco = areaRiscoService.getAreaRiscoById(id);
        if (!areaRisco.isPresent()) {
            return new ResponseEntity("Área de Risco não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(AreaRiscoDTO.create(areaRisco.get()));
    }

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody AreaRiscoDTO dto) {
        try {
            AreaRisco areaRisco = converter(dto);
            areaRisco = areaRiscoService.save(areaRisco);
            return new ResponseEntity(AreaRiscoDTO.create(areaRisco), HttpStatus.CREATED);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @Valid @RequestBody AreaRiscoDTO dto) {
        if (!areaRiscoService.getAreaRiscoById(id).isPresent()) {
            return new ResponseEntity("Área de Risco não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            AreaRisco areaRisco = converter(dto);
            areaRisco.setId(id);
            areaRisco = areaRiscoService.save(areaRisco);
            return ResponseEntity.ok(AreaRiscoDTO.create(areaRisco));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<AreaRisco> areaRisco = areaRiscoService.getAreaRiscoById(id);
        if (!areaRisco.isPresent()) {
            return new ResponseEntity("Área de Risco não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            areaRiscoService.delete(areaRisco.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public AreaRisco converter(AreaRiscoDTO dto) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        Point point = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );

        AreaRisco areaRisco = new AreaRisco();
        areaRisco.setId(dto.getId());
        areaRisco.setNome(dto.getNome());
        areaRisco.setDescricao(dto.getDescricao());
        areaRisco.setNivelRisco(dto.getNivelRisco());
        areaRisco.setLocalizacao(point);

        return areaRisco;
    }

}