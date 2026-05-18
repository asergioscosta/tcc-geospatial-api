package org.backend.arearisco;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.StatusAreaRisco;
import org.backend.exception.RegraNegocioInvalidaException;
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
public class AreaRiscoController {

    private final AreaRiscoService areaRiscoService;

    @GetMapping
    public ResponseEntity<List<AreaRiscoDTO>> get() {
        List<AreaRisco> areasRisco = areaRiscoService.getAreasRisco();

        return ResponseEntity.ok(
                areasRisco.stream()
                        .map(AreaRiscoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<AreaRisco> areaRisco = areaRiscoService.getAreaRiscoById(id);

        if (areaRisco.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Área de Risco não encontrada");
        }

        return ResponseEntity.ok(AreaRiscoDTO.create(areaRisco.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody AreaRiscoDTO dto) {
        try {
            AreaRisco areaRisco = converter(dto);

            areaRisco.setStatusAreaRisco(StatusAreaRisco.EM_MONITORAMENTO);

            areaRisco = areaRiscoService.save(areaRisco);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AreaRiscoDTO.create(areaRisco));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AreaRiscoDTO dto) {
        if (areaRiscoService.getAreaRiscoById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Área de Risco não encontrada");
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<AreaRisco> areaRisco = areaRiscoService.getAreaRiscoById(id);

        if (areaRisco.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Área de Risco não encontrada");
        }

        areaRiscoService.delete(areaRisco.get());
        return ResponseEntity.noContent().build();
    }

    private AreaRisco converter(AreaRiscoDTO dto) {
        AreaRisco areaRisco = new AreaRisco();

        areaRisco.setId(dto.getId());
        areaRisco.setNome(dto.getNome());
        areaRisco.setDescricao(dto.getDescricao());
        areaRisco.setNivelRisco(dto.getNivelRisco());
        areaRisco.setLatitude(dto.getLatitude());
        areaRisco.setLongitude(dto.getLongitude());
        areaRisco.setStatusAreaRisco(dto.getStatusAreaRisco());

        return areaRisco;
    }
}