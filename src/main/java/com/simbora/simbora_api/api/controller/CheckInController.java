package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.CheckInDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.CheckIn;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.service.CheckInService;
import com.simbora.simbora_api.service.IngressoService;
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
@RequestMapping("/api/v1/checkins")
@RequiredArgsConstructor
@Api("API de Check-ins")
@CrossOrigin
public class CheckInController {

    private final CheckInService service;
    private final IngressoService ingressoService;

    @GetMapping
    @ApiOperation("Listar todos os check-ins")
    public ResponseEntity get() {
        List<CheckIn> checkIns = service.getCheckIns();

        return ResponseEntity.ok(
                checkIns.stream()
                        .map(CheckInDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um check-in")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do check-in") Long id) {

        Optional<CheckIn> checkIn = service.getCheckInById(id);

        if (!checkIn.isPresent()) {
            return new ResponseEntity("Check-in não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(CheckInDTO.create(checkIn.get()));
    }

    @PostMapping
    @ApiOperation("Registrar um novo check-in")
    public ResponseEntity post(@RequestBody CheckInDTO dto) {

        try {
            CheckIn checkIn = converter(dto);

            checkIn = service.salvar(checkIn);

            return new ResponseEntity(CheckInDTO.create(checkIn), HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public CheckIn converter(CheckInDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        CheckIn checkIn = modelMapper.map(dto, CheckIn.class);

        Optional<Ingresso> ingresso = ingressoService.getIngressoById(dto.getIdIngresso());

        if (!ingresso.isPresent()) {
            throw new RegraNegocioException("Ingresso não encontrado");
        }

        checkIn.setIngresso(ingresso.get());

        return checkIn;
    }
}