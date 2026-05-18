package org.backend.pontocritico;

import jakarta.transaction.Transactional;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PontoCriticoService {

    private final PontoCriticoRepository pontoCriticoRepository;

    public PontoCriticoService(PontoCriticoRepository pontoCriticoRepository) {
        this.pontoCriticoRepository = pontoCriticoRepository;
    }

    public List<PontoCritico> getPontosCriticos() {
        return pontoCriticoRepository.findAll();
    }

    public Optional<PontoCritico> getPontoCriticoById(Long id) {
        return pontoCriticoRepository.findById(id);
    }

    @Transactional
    public PontoCritico save(PontoCritico pontoCritico) {
        validar(pontoCritico);
        return pontoCriticoRepository.save(pontoCritico);
    }

    @Transactional
    public void delete(PontoCritico pontoCritico) {
        if (pontoCritico.getId() == null) {
            throw new RegraNegocioInvalidaException("ID não pode ser nulo");
        }

        pontoCriticoRepository.delete(pontoCritico);
    }

    public void validar(PontoCritico pontoCritico) {
        if (pontoCritico.getNomePontoCritico() == null || pontoCritico.getNomePontoCritico().isBlank()) {
            throw new RegraNegocioInvalidaException("Nome do Ponto Crítico inválido");
        }

        if (pontoCritico.getDescricaoPontoCritico() == null || pontoCritico.getDescricaoPontoCritico().isBlank()) {
            throw new RegraNegocioInvalidaException("Descrição do Ponto Crítico inválida");
        }

        if (pontoCritico.getTipoPontoCritico() == null) {
            throw new RegraNegocioInvalidaException("Tipo de Ponto Crítico inválido");
        }

        if (pontoCritico.getStatusPontoCritico() == null) {
            throw new RegraNegocioInvalidaException("Status do Ponto Crítico inválido");
        }

        if (pontoCritico.getLatitude() == null || pontoCritico.getLatitude() < -90 || pontoCritico.getLatitude() > 90) {
            throw new RegraNegocioInvalidaException("Latitude inválida");
        }

        if (pontoCritico.getLongitude() == null || pontoCritico.getLongitude() < -180 || pontoCritico.getLongitude() > 180) {
            throw new RegraNegocioInvalidaException("Longitude inválida");
        }
    }
}