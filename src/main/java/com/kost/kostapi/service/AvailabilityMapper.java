package com.kost.kostapi.service;

import com.kost.kostapi.dto.availability.AvailabilityResponse;
import com.kost.kostapi.entity.AvailabilityRequest;

public final class AvailabilityMapper {

    private AvailabilityMapper() {
    }

    public static AvailabilityResponse toResponse(
            AvailabilityRequest request
    ) {

        return new AvailabilityResponse(

                request.getId(),

                request.getKost().getId(),

                request.getUser().getId(),

                request.getCreatedAt()
        );
    }
}