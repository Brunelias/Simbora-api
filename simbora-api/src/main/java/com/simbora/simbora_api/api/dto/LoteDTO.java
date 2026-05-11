package com.simbora.simbora_api.api.dto;

import com.simbora.simbora_api.model.entity.Lote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteDTO {

    private Long id;
    private String nome;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer quantidade;
    private Double precoUnitario;
    private Long idEvento;

    public static LoteDTO create(Lote lote) {
        ModelMapper modelMapper = new ModelMapper();
        LoteDTO dto = modelMapper.map(lote, LoteDTO.class);
        dto.idEvento = lote.getEvento().getId();
        return dto;
    }
}