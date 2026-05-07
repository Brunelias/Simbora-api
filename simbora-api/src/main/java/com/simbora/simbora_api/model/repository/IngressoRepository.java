package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
}