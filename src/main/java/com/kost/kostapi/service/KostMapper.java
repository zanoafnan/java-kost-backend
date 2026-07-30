package com.kost.kostapi.service;

import com.kost.kostapi.dto.kost.KostResponse;
import com.kost.kostapi.entity.Kost;

public final class KostMapper {

    private KostMapper() {
    }

    public static KostResponse toResponse(Kost kost) {

        return new KostResponse(

                kost.getId(),

                kost.getName(),

                kost.getDescription(),

                kost.getLocation(),

                kost.getPrice(),

                kost.getOwner().getId()
        );
    }
}