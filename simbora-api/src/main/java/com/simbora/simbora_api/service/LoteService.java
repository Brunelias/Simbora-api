package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.model.repository.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LoteService {

    private LoteRepository repository;

    public LoteService(LoteRepository repository) {
        this.repository = repository;
    }

    public List<Lote> getLotes() {
        return repository.findAll();
    }

    public Optional<Lote> getLoteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Lote salvar(Lote lote) {
        validar(lote);
        return repository.save(lote);
    }

    @Transactional
    public void excluir(Lote lote) {
        Objects.requireNonNull(lote.getId());
        repository.delete(lote);
    }

    public void validar(Lote lote) {

        if (lote.getNome() == null || lote.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (lote.getDataInicio() == null) {
            throw new RegraNegocioException("Data de início inválida");
        }

        if (lote.getDataFim() == null) {
            throw new RegraNegocioException("Data de fim inválida");
        }

        if (lote.getQuantidade() == null || lote.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }

        if (lote.getPrecoUnitario() == null || lote.getPrecoUnitario() <= 0) {
            throw new RegraNegocioException("Preço unitário inválido");
        }

        if (lote.getEvento() == null || lote.getEvento().getId() == null) {
            throw new RegraNegocioException("Evento inválido");
        }
    }
}
