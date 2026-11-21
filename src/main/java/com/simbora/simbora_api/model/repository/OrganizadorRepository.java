package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    Optional<Organizador> findByEmail(String email);
}