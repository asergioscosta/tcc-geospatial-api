package org.backend.pontocritico;

import jakarta.transaction.Transactional;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PontoCriticoService {


    private PontoCriticoRepository pontoCriticoRepository;

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

        if (pontoCritico.getDescricaoPontoCritico() == null) {
            throw new RegraNegocioInvalidaException("Descrição do Ponto Crítico inválido");
        }

        if (pontoCritico.getLocalizacao() == null) {
            throw new RegraNegocioInvalidaException("Localização inválida");
        }

        if (pontoCritico.getTipoPontoCritico() == null) {
            throw new RegraNegocioInvalidaException("Tipo de Ponto Crítico inválido");
        }

        if (pontoCritico.getStatusPontoCritico() == null) {
            throw new RegraNegocioInvalidaException("Status do Ponto Crítico inválido");
        }

        double latitude = pontoCritico.getLocalizacao().getY();
        double longitude = pontoCritico.getLocalizacao().getX();

        if (latitude < -90 || latitude > 90) {
            throw new RegraNegocioInvalidaException("Latitude inválida");
        }

        if (longitude < -180 || longitude > 180) {
            throw new RegraNegocioInvalidaException("Longitude inválida");
        }
    }
}