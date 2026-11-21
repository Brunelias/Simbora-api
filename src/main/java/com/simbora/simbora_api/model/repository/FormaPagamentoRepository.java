package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
}