package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByEmail(String email);
}