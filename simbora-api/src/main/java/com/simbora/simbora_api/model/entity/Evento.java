package com.simbora.simbora_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String local;
    private Integer capacidadeMaxima;
    private String status;

    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private Organizador organizador;

    @JsonIgnore
    @OneToMany(mappedBy = "evento")
    private List<Lote> lotes;

}