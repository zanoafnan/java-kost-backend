package com.kost.kostapi.repository;

import com.kost.kostapi.entity.AvailabilityRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRequestRepository
        extends JpaRepository<AvailabilityRequest, Long> {
}