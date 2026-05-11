package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.CheckIn;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.model.repository.CheckInRepository;
import com.simbora.simbora_api.model.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CheckInService {

    private CheckInRepository repository;
    private IngressoRepository ingressoRepository;

    public CheckInService(CheckInRepository repository, IngressoRepository ingressoRepository) {
        this.repository = repository;
        this.ingressoRepository = ingressoRepository;
    }

    public List<CheckIn> getCheckIns() {
        return repository.findAll();
    }

    public Optional<CheckIn> getCheckInById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CheckIn salvar(CheckIn checkIn) {
        validar(checkIn);

        Ingresso ingresso = checkIn.getIngresso();

        if ("USADO".equals(ingresso.getStatus())) {
            throw new RegraNegocioException("Ingresso já utilizado");
        }

        if ("CANCELADO".equals(ingresso.getStatus())) {
            throw new RegraNegocioException("Ingresso cancelado");
        }

        checkIn.setDataCheckIn(LocalDateTime.now());
        ingresso.setStatus("USADO");

        ingressoRepository.save(ingresso);

        return repository.save(checkIn);
    }

    public void validar(CheckIn checkIn) {

        if (checkIn.getIngresso() == null || checkIn.getIngresso().getId() == null) {
            throw new RegraNegocioException("Ingresso inválido");
        }
    }
}