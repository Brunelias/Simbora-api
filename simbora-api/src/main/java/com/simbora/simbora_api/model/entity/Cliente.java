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

public class Cliente extends Pessoa{

    private String telefone;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Ingresso> ingressos;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Compra> compras;
}
