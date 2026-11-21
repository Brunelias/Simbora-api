package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.FormaPagamento;
import com.simbora.simbora_api.model.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FormaPagamentoService {

    private FormaPagamentoRepository repository;

    public FormaPagamentoService(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<FormaPagamento> getFormasPagamento() {
        return repository.findAll();
    }

    public Optional<FormaPagamento> getFormaPagamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        validar(formaPagamento);
        return repository.save(formaPagamento);
    }

    @Transactional
    public void excluir(FormaPagamento formaPagamento) {
        Objects.requireNonNull(formaPagamento.getId());
        repository.delete(formaPagamento);
    }

    public void validar(FormaPagamento formaPagamento) {

        if (formaPagamento.getNome() == null || formaPagamento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Forma de pagamento inválida");
        }
    }
}