package com.kost.kostapi.dto.auth;

import com.kost.kostapi.enums.UserRole;

public record UserResponse(

        Long id,

        String name,

        String email,

        UserRole role,

        Integer credit

) {
}