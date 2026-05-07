package com.simbora.simbora_api.api.DTO;

import com.simbora.simbora_api.model.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminDTO {

    private Long id;
    private String nome;
    private String email;
    private String celular;

    public static AdminDTO create(Admin admin) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(admin, AdminDTO.class);
    }
}