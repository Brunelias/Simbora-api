package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByClienteEmail(String email);

    @Query("SELECT DISTINCT c FROM Compra c " +
            "JOIN c.itens i " +
            "JOIN i.lote l " +
            "JOIN l.evento e " +
            "WHERE e.organizador.email = :email")
    List<Compra> findByOrganizadorEmail(@Param("email") String email);
}