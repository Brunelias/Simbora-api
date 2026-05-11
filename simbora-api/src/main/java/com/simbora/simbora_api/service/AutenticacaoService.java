package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.SenhaInvalidaException;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Pessoa;
import com.simbora.simbora_api.model.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AutenticacaoService {

    private PessoaRepository repository;

    public AutenticacaoService(PessoaRepository repository) {
        this.repository = repository;
    }

    public Optional<Pessoa> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    public Pessoa autenticar(String email, String senha) {

        Optional<Pessoa> pessoa = repository.findByEmail(email);

        if (!pessoa.isPresent()) {
            throw new RegraNegocioException("Usuário não encontrado");
        }

        if (!pessoa.get().getSenha().equals(senha)) {
            throw new SenhaInvalidaException();
        }

        return pessoa.get();
    }

    @Transactional
    public Pessoa alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmacaoNovaSenha) {

        Optional<Pessoa> pessoa = repository.findById(id);

        if (!pessoa.isPresent()) {
            throw new RegraNegocioException("Pessoa não encontrada");
        }

        Pessoa pessoaEncontrada = pessoa.get();

        if (!pessoaEncontrada.getSenha().equals(senhaAtual)) {
            throw new SenhaInvalidaException();
        }

        if (novaSenha == null || novaSenha.trim().equals("")) {
            throw new SenhaInvalidaException();
        }

        if (confirmacaoNovaSenha == null || confirmacaoNovaSenha.trim().equals("")) {
            throw new RegraNegocioException("Confirmação de senha inválida");
        }

        if (!novaSenha.equals(confirmacaoNovaSenha)) {
            throw new RegraNegocioException("Confirmação de senha inválida");
        }

        pessoaEncontrada.setSenha(novaSenha);

        return repository.save(pessoaEncontrada);
    }
}