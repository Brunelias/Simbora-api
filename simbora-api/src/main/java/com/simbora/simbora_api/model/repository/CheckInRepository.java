package com.simbora.simbora_api.model.repository;

import com.simbora.simbora_api.model.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
}