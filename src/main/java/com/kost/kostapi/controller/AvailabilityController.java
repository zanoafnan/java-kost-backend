package com.kost.kostapi.controller;

import com.kost.kostapi.dto.availability.AvailabilityResponse;
import com.kost.kostapi.dto.availability.CreateAvailabilityRequest;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityResponse create(

            @AuthenticationPrincipal
            User user,

            @Valid
            @RequestBody
            CreateAvailabilityRequest request

    ) {

        return availabilityService.create(
                user,
                request.kostId()
        );
    }

}