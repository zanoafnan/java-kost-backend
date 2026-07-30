package com.kost.kostapi.dto.kost;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record UpdateKostRequest(

        @Size(max = 100) String name,

        String description,

        @Size(max = 255) String location,

        @DecimalMin("1") BigDecimal price

) {
}