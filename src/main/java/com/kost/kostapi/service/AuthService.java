package com.kost.kostapi.service;

import com.kost.kostapi.dto.auth.AuthResponse;
import com.kost.kostapi.dto.auth.LoginRequest;
import com.kost.kostapi.dto.auth.RegisterRequest;
import com.kost.kostapi.dto.auth.UserResponse;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.CreditAmount;
import com.kost.kostapi.repository.UserRepository;
import com.kost.kostapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        int credit = switch (request.role()) {
            case OWNER -> CreditAmount.OWNER.getValue();
            case REGULAR -> CreditAmount.REGULAR.getValue();
            case PREMIUM -> CreditAmount.PREMIUM.getValue();
        };

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .credit(credit)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                toUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                toUserResponse(user));
    }

    public UserResponse me(User user) {
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCredit());
    }
}