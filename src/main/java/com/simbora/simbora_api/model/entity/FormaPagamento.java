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

public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex: CARTAO, PIX, BOLETO
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "formaPagamento")
    private List<Compra> compras;
}
