package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
}