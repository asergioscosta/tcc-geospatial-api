package org.backend.pontocritico;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.StatusPontoCritico;
import org.backend.exception.RegraNegocioInvalidaException;
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
public class PontoCriticoController {

    private final PontoCriticoService pontoCriticoService;

    @GetMapping
    public ResponseEntity<List<PontoCriticoDTO>> get() {
        List<PontoCritico> pontosCriticos = pontoCriticoService.getPontosCriticos();

        return ResponseEntity.ok(
                pontosCriticos.stream()
                        .map(PontoCriticoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<PontoCritico> pontoCritico = pontoCriticoService.getPontoCriticoById(id);

        if (pontoCritico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ponto Crítico não encontrado");
        }

        return ResponseEntity.ok(PontoCriticoDTO.create(pontoCritico.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody PontoCriticoDTO dto) {
        try {
            PontoCritico pontoCritico = converter(dto, true, null);
            pontoCritico = pontoCriticoService.save(pontoCritico);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PontoCriticoDTO.create(pontoCritico));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody PontoCriticoDTO dto) {
        Optional<PontoCritico> existente = pontoCriticoService.getPontoCriticoById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ponto Crítico não encontrado");
        }

        try {
            PontoCritico pontoCritico = converter(dto, false, existente.get());
            pontoCritico.setId(id);
            pontoCritico = pontoCriticoService.save(pontoCritico);

            return ResponseEntity.ok(PontoCriticoDTO.create(pontoCritico));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<PontoCritico> pontoCritico = pontoCriticoService.getPontoCriticoById(id);

        if (pontoCritico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ponto Crítico não encontrado");
        }

        pontoCriticoService.delete(pontoCritico.get());
        return ResponseEntity.noContent().build();
    }

    private PontoCritico converter(PontoCriticoDTO dto, boolean cadastroPublico, PontoCritico existente) {
        PontoCritico pontoCritico = new PontoCritico();

        pontoCritico.setId(dto.getId());
        pontoCritico.setNomePontoCritico(dto.getNomePontoCritico());
        pontoCritico.setDescricaoPontoCritico(dto.getDescricaoPontoCritico());
        pontoCritico.setTipoPontoCritico(dto.getTipoPontoCritico());
        pontoCritico.setNivelRisco(dto.getNivelRisco());
        pontoCritico.setLatitude(dto.getLatitude());
        pontoCritico.setLongitude(dto.getLongitude());

        if (cadastroPublico) {
            pontoCritico.setStatusPontoCritico(StatusPontoCritico.ATIVO);
        } else {
            pontoCritico.setStatusPontoCritico(
                    dto.getStatusPontoCritico() != null
                            ? dto.getStatusPontoCritico()
                            : existente.getStatusPontoCritico()
            );
        }

        return pontoCritico;
    }
}
