package com.simbora.simbora_api.api.dto;

import com.simbora.simbora_api.model.entity.Organizador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrganizadorDTO {

    private Long id;
    private String nome;
    private String email;
    private String celular;
    private String documento;
    private String senha;

    public static OrganizadorDTO create(Organizador organizador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(organizador, OrganizadorDTO.class);
    }
}