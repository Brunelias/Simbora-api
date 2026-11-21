package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.IngressoDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.service.IngressoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ingressos")
@RequiredArgsConstructor
@Api("API de Ingressos")
@CrossOrigin
public class IngressoController {

    private final IngressoService service;

    @GetMapping
    @ApiOperation("Listar todos os ingressos")
    public ResponseEntity get() {
        List<Ingresso> ingressos = service.getIngressos();

        return ResponseEntity.ok(
                ingressos.stream()
                        .map(IngressoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um ingresso")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do ingresso") Long id) {

        Optional<Ingresso> ingresso = service.getIngressoById(id);

        if (!ingresso.isPresent()) {
            return new ResponseEntity("Ingresso não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(IngressoDTO.create(ingresso.get()));
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um ingresso")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Ingresso> ingresso = service.getIngressoById(id);

        if (!ingresso.isPresent()) {
            return new ResponseEntity("Ingresso não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(ingresso.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}