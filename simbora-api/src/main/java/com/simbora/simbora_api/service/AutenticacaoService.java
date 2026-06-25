package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.SenhaInvalidaException;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Pessoa;
import com.simbora.simbora_api.model.entity.Admin;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.model.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    private PessoaRepository repository;

    @Autowired
    private PasswordEncoder encoder;

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

        boolean senhaValida =
                encoder.matches(senha, pessoa.get().getSenha());

        if (!senhaValida) {
            throw new SenhaInvalidaException();
        }

        return pessoa.get();
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Pessoa pessoa = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado"));

        String[] roles;

        if (pessoa instanceof Admin) {
            roles = new String[]{"ADMIN"};
        } else if (pessoa instanceof Organizador) {
            roles = new String[]{"ORGANIZADOR"};
        } else if (pessoa instanceof Cliente){
            roles = new String[]{"CLIENTE"};
        } else throw new RegraNegocioException("Tipo de usuário inválido");


        return User
                .builder()
                .username(pessoa.getEmail())
                .password(pessoa.getSenha())
                .roles(roles)
                .build();
    }

    @Transactional
    public Pessoa alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmacaoNovaSenha) {

        Optional<Pessoa> pessoa = repository.findById(id);

        if (!pessoa.isPresent()) {
            throw new RegraNegocioException("Pessoa não encontrada");
        }

        Pessoa pessoaEncontrada = pessoa.get();

        if (!encoder.matches(
                senhaAtual,
                pessoaEncontrada.getSenha())) {
            throw new SenhaInvalidaException();
        }

        if (novaSenha == null || novaSenha.trim().equals("")) {
            throw new SenhaInvalidaException();
        }

        if (confirmacaoNovaSenha == null ||
                confirmacaoNovaSenha.trim().equals("")) {
            throw new RegraNegocioException("Confirmação de senha inválida");
        }

        if (!novaSenha.equals(confirmacaoNovaSenha)) {
            throw new RegraNegocioException("Confirmação de senha inválida");
        }

        pessoaEncontrada.setSenha(
                encoder.encode(novaSenha)
        );

        return repository.save(pessoaEncontrada);
    }
}