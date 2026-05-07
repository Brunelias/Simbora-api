package com.simbora.simbora_api.api.DTO;

import com.simbora.simbora_api.model.entity.Ingresso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class IngressoDTO {

    private Long id;
    private LocalDateTime dataGeracao;
    private String codigo;
    private String status;
    private Long idLote;
    private Long idCliente;
    private Long idItemCompra;

    public static IngressoDTO create(Ingresso ingresso) {
        ModelMapper modelMapper = new ModelMapper();
        IngressoDTO dto = modelMapper.map(ingresso, IngressoDTO.class);
        dto.idLote = ingresso.getLote().getId();
        dto.idCliente = ingresso.getCliente().getId();
        dto.idItemCompra = ingresso.getItemCompra().getId();
        return dto;
    }
}