package com.kost.kostapi.dto.availability;

import java.time.LocalDateTime;

public record AvailabilityResponse(

        Long id,

        Long kostId,

        Long userId,

        LocalDateTime createdAt

) {
}