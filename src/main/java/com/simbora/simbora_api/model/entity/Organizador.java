package com.simbora.simbora_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Organizador extends Pessoa {

    private String documento;

    @JsonIgnore
    @OneToMany(mappedBy = "organizador")
    private List<Evento> eventos;

}