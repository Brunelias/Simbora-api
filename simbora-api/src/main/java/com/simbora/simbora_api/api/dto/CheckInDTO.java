package com.simbora.simbora_api.api.dto;

import com.simbora.simbora_api.model.entity.CheckIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CheckInDTO {

    private Long id;
    private LocalDateTime dataCheckIn;
    private Long idIngresso;

    public static CheckInDTO create(CheckIn checkIn) {
        ModelMapper modelMapper = new ModelMapper();
        CheckInDTO dto = modelMapper.map(checkIn, CheckInDTO.class);
        dto.idIngresso = checkIn.getIngresso().getId();
        return dto;
    }
}