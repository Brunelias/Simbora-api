package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.exception.SenhaInvalidaException;
import com.simbora.simbora_api.model.entity.Admin;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.model.entity.Pessoa;
import com.simbora.simbora_api.security.JwtService;
import com.simbora.simbora_api.service.AutenticacaoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Api("API de Autenticação")
@CrossOrigin
public class AutenticacaoController {

    private final AutenticacaoService service;
    private final JwtService jwtService;

    @PostMapping
    @ApiOperation("Autenticar usuário")
    public ResponseEntity autenticar(
            @RequestBody Map<String, String> dados) {

        try {

            String email = dados.get("email");
            String senha = dados.get("senha");

            Pessoa pessoa =
                    service.autenticar(email, senha);

            String token =
                    jwtService.gerarToken(pessoa);

            String tipo;

            if (pessoa instanceof Admin) {
                tipo = "ADMIN";
            }
            else if (pessoa instanceof Organizador) {
                tipo = "ORGANIZADOR";
            }
            else {
                tipo = "CLIENTE";
            }


            Map<String, Object> resposta =
                    new HashMap<>();

            resposta.put("id", pessoa.getId());
            resposta.put("nome", pessoa.getNome());
            resposta.put("email", pessoa.getEmail());
            resposta.put("tipo", tipo);
            resposta.put("token", token);

            return ResponseEntity.ok(resposta);

        } catch (RegraNegocioException |
                 SenhaInvalidaException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/alterar-senha")
    @ApiOperation("Alterar senha do usuário autenticado")
    public ResponseEntity alterarSenha(
            @RequestBody Map<String, String> dados,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            Optional<Pessoa> pessoa =
                    service.buscarPorEmail(email);

            if (!pessoa.isPresent()) {
                return new ResponseEntity(
                        "Usuário não encontrado",
                        HttpStatus.NOT_FOUND);
            }

            service.alterarSenha(
                    pessoa.get().getId(),
                    dados.get("senhaAtual"),
                    dados.get("novaSenha"),
                    dados.get("confirmacaoNovaSenha")
            );

            return ResponseEntity.ok(
                    "Senha alterada com sucesso"
            );

        } catch (RegraNegocioException |
                 SenhaInvalidaException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/me")
    @ApiOperation("Obter usuário autenticado")
    public ResponseEntity me(
            Authentication authentication) {

        String email = authentication.getName();

        Optional<Pessoa> pessoa =
                service.buscarPorEmail(email);

        if (!pessoa.isPresent()) {
            return new ResponseEntity(
                    "Usuário não encontrado",
                    HttpStatus.NOT_FOUND);
        }

        Pessoa usuario = pessoa.get();

        String tipo;

        if (usuario instanceof Admin) {
            tipo = "ADMIN";
        }
        else if (usuario instanceof Organizador) {
            tipo = "ORGANIZADOR";
        }
        else {
            tipo = "CLIENTE";
        }

        Map<String, Object> resposta =
                new HashMap<>();

        resposta.put("id", usuario.getId());
        resposta.put("nome", usuario.getNome());
        resposta.put("email", usuario.getEmail());
        resposta.put("tipo", tipo);

        return ResponseEntity.ok(resposta);
    }
}