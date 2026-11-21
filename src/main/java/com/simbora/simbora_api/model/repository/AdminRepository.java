package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}