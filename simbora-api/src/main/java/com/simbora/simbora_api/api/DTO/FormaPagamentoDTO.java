package com.simbora.simbora_api.api.DTO;

import com.simbora.simbora_api.model.entity.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FormaPagamentoDTO {

    private Long id;
    private String nome;

    public static FormaPagamentoDTO create(FormaPagamento formaPagamento) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
    }
}