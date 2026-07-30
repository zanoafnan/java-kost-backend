package com.kost.kostapi.dto.kost;

public record SearchKostRequest(

        String name,

        String location,

        Integer minPrice,

        Integer maxPrice,

        String sort

) {
}