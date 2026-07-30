package com.kost.kostapi.controller;

import com.kost.kostapi.dto.auth.AuthResponse;
import com.kost.kostapi.dto.auth.LoginRequest;
import com.kost.kostapi.dto.auth.RegisterRequest;
import com.kost.kostapi.dto.auth.UserResponse;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(
            @AuthenticationPrincipal User user
    ) {
        return authService.me(user);
    }
}