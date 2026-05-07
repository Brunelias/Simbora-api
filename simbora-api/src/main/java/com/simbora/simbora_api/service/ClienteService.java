package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {

    private ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> getClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        repository.delete(cliente);
    }

    public void validar(Cliente cliente) {

        if (cliente.getNome() == null || cliente.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }

        if (cliente.getCelular() == null || cliente.getCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }
    }
}