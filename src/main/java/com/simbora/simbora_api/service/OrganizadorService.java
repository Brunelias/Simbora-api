package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.model.repository.OrganizadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class OrganizadorService {

    private OrganizadorRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public OrganizadorService(OrganizadorRepository repository) {
        this.repository = repository;
    }

    public List<Organizador> getOrganizadores() {
        return repository.findAll();
    }

    public Optional<Organizador> getOrganizadorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Organizador salvar(Organizador organizador) {
        validar(organizador);

        organizador.setSenha(encoder.encode(organizador.getSenha()));

        return repository.save(organizador);
    }

    @Transactional
    public Organizador atualizar(Organizador organizador) {
        Organizador organizadorBanco = repository.findById(organizador.getId()).orElseThrow(() ->
                        new RegraNegocioException("Organizador não encontrado"));

        validar(organizador);
        organizador.setSenha(organizadorBanco.getSenha());

        return repository.save(organizador);
    }

    @Transactional
    public Organizador atualizarPorEmail(String email, Organizador organizadorAtualizado) {
        Organizador organizadorBanco = repository.findByEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Organizador não encontrado"));

        organizadorBanco.setNome(organizadorAtualizado.getNome());
        organizadorBanco.setCelular(organizadorAtualizado.getCelular());

        return repository.save(organizadorBanco);
    }

    @Transactional
    public void excluir(Organizador organizador) {
        Objects.requireNonNull(organizador.getId());
        repository.delete(organizador);
    }

    public void validar(Organizador organizador) {

        if (organizador.getNome() == null || organizador.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (organizador.getEmail() == null || organizador.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }

        if (organizador.getCelular() == null || organizador.getCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }

        if (organizador.getDocumento() == null || organizador.getDocumento().trim().equals("")) {
            throw new RegraNegocioException("Documento inválido");
        }
        if (organizador.getSenha() == null ||organizador.getSenha().trim().equals("")) {
            throw new RegraNegocioException("Senha inválida");
        }
    }
}