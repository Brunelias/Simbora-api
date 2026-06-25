package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class ClienteService {

    private ClienteRepository repository;

    @Autowired
    private PasswordEncoder encoder;

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
        cliente.setSenha(encoder.encode(cliente.getSenha()));

        return repository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Cliente clienteBanco = repository.findById(cliente.getId()).orElseThrow(() ->
                        new RegraNegocioException("Cliente não encontrado"));

        validar(cliente);
        cliente.setSenha(clienteBanco.getSenha());

        return repository.save(cliente);
    }

    @Transactional
    public Cliente atualizarPorEmail(String email, Cliente clienteAtualizado) {
        Cliente clienteBanco = repository.findByEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado"));

        clienteBanco.setNome(clienteAtualizado.getNome());
        clienteBanco.setCelular(clienteAtualizado.getCelular());

        return repository.save(clienteBanco);
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
        if (cliente.getSenha() == null ||
                cliente.getSenha().trim().equals("")) {
            throw new RegraNegocioException("Senha inválida");
        }
    }
}