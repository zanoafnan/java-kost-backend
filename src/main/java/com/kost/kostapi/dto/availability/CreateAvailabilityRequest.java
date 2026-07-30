package com.kost.kostapi.dto.availability;

import jakarta.validation.constraints.NotNull;

public record CreateAvailabilityRequest(

        @NotNull
        Long kostId

) {
}