package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.OrganizadorDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.service.OrganizadorService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/organizadores")
@RequiredArgsConstructor
@Api("API de Organizadores")
@CrossOrigin
public class OrganizadorController {

    private final OrganizadorService service;

    @GetMapping
    public ResponseEntity get() {

        List<Organizador> organizadores = service.getOrganizadores();

        return ResponseEntity.ok(
                organizadores.stream()
                        .map(OrganizadorDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um organizador")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do organizador") Long id) {

        Optional<Organizador> organizador = service.getOrganizadorById(id);

        if (!organizador.isPresent()) {
            return new ResponseEntity("Organizador não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(OrganizadorDTO.create(organizador.get()));
    }

    @PostMapping
    @ApiOperation("Salvar um novo organizador")
    public ResponseEntity post(@RequestBody OrganizadorDTO dto) {

        try {

            Organizador organizador = converter(dto);

            organizador = service.salvar(organizador);

            return new ResponseEntity(
                    OrganizadorDTO.create(organizador),
                    HttpStatus.CREATED
            );

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um organizador")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody OrganizadorDTO dto) {

        if (!service.getOrganizadorById(id).isPresent()) {
            return new ResponseEntity("Organizador não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            Organizador organizador = converter(dto);

            organizador.setId(id);

            organizador = service.atualizar(organizador);

            return ResponseEntity.ok(OrganizadorDTO.create(organizador));

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/me")
    @ApiOperation("Atualizar próprio perfil")
    public ResponseEntity atualizarMe(@RequestBody OrganizadorDTO dto, Authentication authentication) {
        try {
            String email = authentication.getName();
            Organizador organizadorAtualizado = converter(dto);
            Organizador salvo = service.atualizarPorEmail(email, organizadorAtualizado);
            return ResponseEntity.ok(OrganizadorDTO.create(salvo));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um organizador")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Organizador> organizador = service.getOrganizadorById(id);

        if (!organizador.isPresent()) {
            return new ResponseEntity("Organizador não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            service.excluir(organizador.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Organizador converter(OrganizadorDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(dto, Organizador.class);
    }
}