package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long> {
}