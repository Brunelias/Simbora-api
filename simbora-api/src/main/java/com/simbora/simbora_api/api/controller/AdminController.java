package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.AdminDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Admin;
import com.simbora.simbora_api.service.AdminService;
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
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Api("API de Administradores")
@CrossOrigin
public class AdminController {

    private final AdminService service;

    @GetMapping
    public ResponseEntity get() {

        List<Admin> admins = service.getAdmins();

        return ResponseEntity.ok(
                admins.stream()
                        .map(AdminDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um administrador")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do administrador") Long id) {

        Optional<Admin> admin = service.getAdminById(id);

        if (!admin.isPresent()) {
            return new ResponseEntity("Administrador não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(AdminDTO.create(admin.get()));
    }

    @PostMapping
    @ApiOperation("Salvar um novo administrador")
    public ResponseEntity post(@RequestBody AdminDTO dto) {

        try {

            Admin admin = converter(dto);

            admin = service.salvar(admin);

            return new ResponseEntity(
                    AdminDTO.create(admin),
                    HttpStatus.CREATED
            );

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um administrador")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody AdminDTO dto) {

        if (!service.getAdminById(id).isPresent()) {
            return new ResponseEntity("Administrador não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            Admin admin = converter(dto);

            admin.setId(id);

            admin = service.salvar(admin);

            return ResponseEntity.ok(AdminDTO.create(admin));

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um administrador")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Admin> admin = service.getAdminById(id);

        if (!admin.isPresent()) {
            return new ResponseEntity("Administrador não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            service.excluir(admin.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Admin converter(AdminDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(dto, Admin.class);
    }
}