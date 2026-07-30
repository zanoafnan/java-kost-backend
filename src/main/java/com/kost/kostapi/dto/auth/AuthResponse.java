package com.kost.kostapi.dto.auth;

public record AuthResponse(

        String token,

        UserResponse user

) {
}