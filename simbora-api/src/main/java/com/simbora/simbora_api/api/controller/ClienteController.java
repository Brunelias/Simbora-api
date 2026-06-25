package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.ClienteDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Api("API de Clientes")
@CrossOrigin
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    public ResponseEntity get() {

        List<Cliente> clientes = service.getClientes();

        return ResponseEntity.ok(
                clientes.stream()
                        .map(ClienteDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do cliente") Long id) {

        Optional<Cliente> cliente = service.getClienteById(id);

        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ClienteDTO.create(cliente.get()));
    }

    @PostMapping
    @ApiOperation("Salvar um novo cliente")
    public ResponseEntity post(@RequestBody ClienteDTO dto) {

        try {

            Cliente cliente = converter(dto);

            cliente = service.salvar(cliente);

            return new ResponseEntity(ClienteDTO.create(cliente), HttpStatus.CREATED);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um cliente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ClienteDTO dto) {

        if (!service.getClienteById(id).isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            Cliente cliente = converter(dto);

            cliente.setId(id);

            cliente = service.atualizar(cliente);

            return ResponseEntity.ok(ClienteDTO.create(cliente));

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/me")
    @ApiOperation("Atualizar próprio perfil")
    public ResponseEntity atualizarMe(@RequestBody ClienteDTO dto, Authentication authentication) {
        try {
            String email = authentication.getName();
            Cliente clienteAtualizado = converter(dto);
            Cliente salvo = service.atualizarPorEmail(email, clienteAtualizado);
            return ResponseEntity.ok(ClienteDTO.create(salvo));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um cliente")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Cliente> cliente = service.getClienteById(id);

        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            service.excluir(cliente.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(dto, Cliente.class);
    }
}