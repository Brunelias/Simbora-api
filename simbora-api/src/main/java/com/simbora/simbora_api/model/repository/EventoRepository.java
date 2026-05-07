package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}