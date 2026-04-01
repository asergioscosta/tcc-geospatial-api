package org.backend.arearisco;

import jakarta.transaction.Transactional;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaRiscoService {

    private AreaRiscoRepository areaRiscoRepository;

    public AreaRiscoService(AreaRiscoRepository areaRiscoRepository) {
        this.areaRiscoRepository = areaRiscoRepository;
    }

    public List<AreaRisco> getAreasRisco() {
        return areaRiscoRepository.findAll();
    }

    public Optional<AreaRisco> getAreaRiscoById(Long id) {
        return areaRiscoRepository.findById(id);
    }

    @Transactional
    public AreaRisco save(AreaRisco areaRisco) {
        validar(areaRisco);
        return areaRiscoRepository.save(areaRisco);
    }

    @Transactional
    public void delete(AreaRisco areaRisco) {
        if (areaRisco.getId() == null) {
            throw new RegraNegocioInvalidaException("ID não pode ser nulo");
        }
        areaRiscoRepository.delete(areaRisco);
    }

    public void validar(AreaRisco areaRisco) {

        if (areaRisco.getNome() == null || areaRisco.getNome().isBlank()) {
            throw new RegraNegocioInvalidaException("Nome inválido");
        }

        if (areaRisco.getDescricao() == null || areaRisco.getDescricao().isBlank()) {
            throw new RegraNegocioInvalidaException("Descrição inválida");
        }

        if (areaRisco.getLocalizacao() == null) {
            throw new RegraNegocioInvalidaException("Localização inválida");
        }

        if (areaRisco.getNivelRisco() == null) {
            throw new RegraNegocioInvalidaException("Nível de risco inválido");
        }

        double latitude = areaRisco.getLocalizacao().getY();
        double longitude = areaRisco.getLocalizacao().getX();

        if (latitude < -90 || latitude > 90) {
            throw new RegraNegocioInvalidaException("Latitude inválida");
        }

        if (longitude < -180 || longitude > 180) {
            throw new RegraNegocioInvalidaException("Longitude inválida");
        }
    }
}