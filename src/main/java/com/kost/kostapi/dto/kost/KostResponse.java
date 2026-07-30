package com.kost.kostapi.dto.kost;

import java.math.BigDecimal;

public record KostResponse(

        Long id,

        String name,

        String description,

        String location,

        BigDecimal price,

        Long ownerId

) {
}