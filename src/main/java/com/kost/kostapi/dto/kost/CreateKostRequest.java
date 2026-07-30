package com.kost.kostapi.dto.kost;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateKostRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        String description,

        @NotBlank
        @Size(max = 255)
        String location,

        @NotNull
        @DecimalMin("1")
        BigDecimal price

) {
}