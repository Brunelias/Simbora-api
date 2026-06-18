package com.simbora.simbora_api.api.dto;

import com.simbora.simbora_api.model.entity.Compra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Long id;

    private LocalDateTime dataCompra;
    private Double valorTotal;
    private String status;

    private Long idCliente;
    private Long idFormaPagamento;

    private List<ItemCompraDTO> itens;

    public static CompraDTO create(Compra compra) {

        ModelMapper modelMapper = new ModelMapper();

        CompraDTO dto = modelMapper.map(compra, CompraDTO.class);

        dto.idCliente = compra.getCliente().getId();

        if (compra.getFormaPagamento() != null) {
            dto.idFormaPagamento = compra.getFormaPagamento().getId();
        }

        if (compra.getItens() != null) {
            dto.itens = compra.getItens()
                    .stream()
                    .map(ItemCompraDTO::create)
                    .collect(Collectors.toList());
        }

        return dto;
    }
}