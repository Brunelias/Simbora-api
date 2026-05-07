package com.simbora.simbora_api.api.DTO;

import com.simbora.simbora_api.model.entity.Evento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EventoDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String local;
    private Integer capacidadeMaxima;
    private String status;
    private Long idOrganizador;

    public static EventoDTO create(Evento evento) {
        ModelMapper modelMapper = new ModelMapper();
        EventoDTO dto = modelMapper.map(evento, EventoDTO.class);
        dto.idOrganizador = evento.getOrganizador().getId();
        return dto;
    }
}
