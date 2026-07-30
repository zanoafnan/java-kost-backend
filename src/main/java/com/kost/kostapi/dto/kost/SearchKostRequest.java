package com.kost.kostapi.dto.kost;

import java.math.BigDecimal;

public record SearchKostRequest(

        String name,

        String location,

        BigDecimal minPrice,

        BigDecimal maxPrice,

        String sort

) {
}