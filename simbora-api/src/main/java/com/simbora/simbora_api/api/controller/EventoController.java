package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.EventoDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Evento;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.service.EventoService;
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

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
@Api("API de Eventos")
@CrossOrigin
public class EventoController {

    private final EventoService service;
    private final OrganizadorService organizadorService;

    @GetMapping
    public ResponseEntity get() {
        List<Evento> eventos = service.getEventos();

        return ResponseEntity.ok(
                eventos.stream()
                        .map(EventoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um evento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Evento encontrado"),
            @ApiResponse(code = 404, message = "Evento não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do evento") Long id) {

        Optional<Evento> evento = service.getEventoById(id);

        if (!evento.isPresent()) {
            return new ResponseEntity("Evento não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(EventoDTO.create(evento.get()));
    }

    @PostMapping
    @ApiOperation("Salvar um novo evento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Evento salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o evento")
    })
    public ResponseEntity post(@RequestBody EventoDTO dto) {

        try {
            Evento evento = converter(dto);

            evento = service.salvar(evento);

            return new ResponseEntity(EventoDTO.create(evento), HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um evento")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EventoDTO dto) {

        if (!service.getEventoById(id).isPresent()) {
            return new ResponseEntity("Evento não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Evento evento = converter(dto);

            evento.setId(id);

            evento = service.salvar(evento);

            return ResponseEntity.ok(EventoDTO.create(evento));

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um evento")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Evento> evento = service.getEventoById(id);

        if (!evento.isPresent()) {
            return new ResponseEntity("Evento não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(evento.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Evento converter(EventoDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        Evento evento = modelMapper.map(dto, Evento.class);

        Optional<Organizador> organizador = organizadorService.getOrganizadorById(dto.getIdOrganizador());

        if (!organizador.isPresent()) {
            throw new RegraNegocioException("Organizador não encontrado");
        }

        evento.setOrganizador(organizador.get());

        return evento;
    }
}