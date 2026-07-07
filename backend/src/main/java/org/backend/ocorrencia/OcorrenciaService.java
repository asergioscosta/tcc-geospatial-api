package org.backend.ocorrencia;

import jakarta.transaction.Transactional;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrenciaRepository.findAll();
    }

    public Optional<Ocorrencia> getOcorrenciaById(Long id) {
        return ocorrenciaRepository.findById(id);
    }

    @Transactional
    public Ocorrencia save(Ocorrencia ocorrencia) {
        validar(ocorrencia);
        return ocorrenciaRepository.save(ocorrencia);
    }

    @Transactional
    public void delete(Ocorrencia ocorrencia) {
        if (ocorrencia.getId() == null) {
            throw new RegraNegocioInvalidaException("ID não pode ser nulo");
        }

        ocorrenciaRepository.delete(ocorrencia);
    }

    public void validar(Ocorrencia ocorrencia) {
        if (ocorrencia.getDescricaoOcorrencia() == null || ocorrencia.getDescricaoOcorrencia().isBlank()) {
            throw new RegraNegocioInvalidaException("Descrição da Ocorrência inválida");
        }

        if (ocorrencia.getPrioridadeOcorrencia() == null) {
            throw new RegraNegocioInvalidaException("Prioridade da Ocorrência inválida");
        }

        if (ocorrencia.getDataHora() == null) {
            throw new RegraNegocioInvalidaException("Data/Hora inválida");
        }

        if (ocorrencia.getTipoOcorrencia() == null) {
            throw new RegraNegocioInvalidaException("Tipo de Ocorrência inválido");
        }

        if (ocorrencia.getStatusOcorrencia() == null) {
            throw new RegraNegocioInvalidaException("Status da Ocorrência inválido");
        }

        if (ocorrencia.getNivelRisco() == null) {
            throw new RegraNegocioInvalidaException("Nível de risco inválido");
        }

        if (ocorrencia.getLatitude() == null || ocorrencia.getLatitude() < -90 || ocorrencia.getLatitude() > 90) {
            throw new RegraNegocioInvalidaException("Latitude inválida");
        }

        if (ocorrencia.getLongitude() == null || ocorrencia.getLongitude() < -180 || ocorrencia.getLongitude() > 180) {
            throw new RegraNegocioInvalidaException("Longitude inválida");
        }
    }
}
