package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.LoteDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Evento;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.service.EventoService;
import com.simbora.simbora_api.service.LoteService;
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
@RequestMapping("/api/v1/lotes")
@RequiredArgsConstructor
@Api("API de Lotes")
@CrossOrigin
public class LoteController {

    private final LoteService service;
    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity get() {
        List<Lote> lotes = service.getLotes();

        return ResponseEntity.ok(
                lotes.stream()
                        .map(LoteDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um lote")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do lote") Long id) {

        Optional<Lote> lote = service.getLoteById(id);

        if (!lote.isPresent()) {
            return new ResponseEntity("Lote não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(LoteDTO.create(lote.get()));
    }

    @PostMapping
    @ApiOperation("Salvar um novo lote")
    public ResponseEntity post(@RequestBody LoteDTO dto) {

        try {
            Lote lote = converter(dto);
            lote = service.salvar(lote);

            return new ResponseEntity(LoteDTO.create(lote), HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um lote")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LoteDTO dto) {

        if (!service.getLoteById(id).isPresent()) {
            return new ResponseEntity("Lote não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Lote lote = converter(dto);
            lote.setId(id);

            lote = service.salvar(lote);

            return ResponseEntity.ok(LoteDTO.create(lote));

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um lote")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Lote> lote = service.getLoteById(id);

        if (!lote.isPresent()) {
            return new ResponseEntity("Lote não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(lote.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Lote converter(LoteDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        Lote lote = modelMapper.map(dto, Lote.class);

        Optional<Evento> evento = eventoService.getEventoById(dto.getIdEvento());

        if (!evento.isPresent()) {
            throw new RegraNegocioException("Evento não encontrado");
        }

        lote.setEvento(evento.get());

        return lote;
    }
}