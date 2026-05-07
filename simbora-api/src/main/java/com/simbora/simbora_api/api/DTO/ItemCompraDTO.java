package com.simbora.simbora_api.api.DTO;

import com.simbora.simbora_api.model.entity.ItemCompra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemCompraDTO {

    private Long id;
    private Integer quantidade;
    private Double valorUnitario;
    private Double valorTotal;
    private Long idCompra;
    private Long idLote;

    public static ItemCompraDTO create(ItemCompra itemCompra) {
        ModelMapper modelMapper = new ModelMapper();
        ItemCompraDTO dto = modelMapper.map(itemCompra, ItemCompraDTO.class);
        dto.idCompra = itemCompra.getCompra().getId();
        dto.idLote = itemCompra.getLote().getId();
        return dto;
    }
}