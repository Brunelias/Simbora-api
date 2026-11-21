package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.model.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IngressoService {

    private IngressoRepository repository;

    public IngressoService(IngressoRepository repository) {
        this.repository = repository;
    }

    public List<Ingresso> getIngressos() {
        return repository.findAll();
    }

    public Optional<Ingresso> getIngressoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Ingresso salvar(Ingresso ingresso) {
        validar(ingresso);
        return repository.save(ingresso);
    }

    @Transactional
    public void excluir(Ingresso ingresso) {
        Objects.requireNonNull(ingresso.getId());
        repository.delete(ingresso);
    }

    public void validar(Ingresso ingresso) {

        if (ingresso.getDataGeracao() == null) {
            throw new RegraNegocioException("Data de geração inválida");
        }

        if (ingresso.getCodigo() == null || ingresso.getCodigo().trim().equals("")) {
            throw new RegraNegocioException("Código inválido");
        }

        if (ingresso.getStatus() == null || ingresso.getStatus().trim().equals("")) {
            throw new RegraNegocioException("Status inválido");
        }

        if (ingresso.getLote() == null || ingresso.getLote().getId() == null) {
            throw new RegraNegocioException("Lote inválido");
        }

        if (ingresso.getCliente() == null || ingresso.getCliente().getId() == null) {
            throw new RegraNegocioException("Cliente inválido");
        }

        if (ingresso.getItemCompra() == null || ingresso.getItemCompra().getId() == null) {
            throw new RegraNegocioException("Item da compra inválido");
        }
    }
}