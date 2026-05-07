package com.simbora.simbora_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer quantidade;
    private Double precoUnitario;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @JsonIgnore
    @OneToMany (mappedBy = "lote")
    private List<Ingresso> ingressos;

    @JsonIgnore
    @OneToMany(mappedBy = "lote")
    private List<ItemCompra> itensCompra;
}
