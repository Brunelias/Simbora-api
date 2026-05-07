package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}