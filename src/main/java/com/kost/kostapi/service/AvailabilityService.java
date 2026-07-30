package com.kost.kostapi.service;

import com.kost.kostapi.dto.availability.AvailabilityResponse;
import com.kost.kostapi.entity.AvailabilityRequest;
import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.UserRole;
import com.kost.kostapi.repository.AvailabilityRequestRepository;
import com.kost.kostapi.repository.KostRepository;
import com.kost.kostapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRequestRepository availabilityRepository;
    private final KostRepository kostRepository;
    private final UserRepository userRepository;

    @Transactional
    public AvailabilityResponse create(
            User user,
            Long kostId
    ) {

        if (user.getRole() == UserRole.OWNER) {
            throw new IllegalStateException(
                    "Owner cannot request availability."
            );
        }

        int cost = switch (user.getRole()) {
            case REGULAR -> 5;
            case PREMIUM -> 2;
            default -> 0;
        };

        if (user.getCredit() < cost) {
            throw new IllegalStateException(
                    "Insufficient credit."
            );
        }

        Kost kost = kostRepository
                .findById(kostId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Kost not found."
                        ));

        user.setCredit(
                user.getCredit() - cost
        );

        userRepository.save(user);

        AvailabilityRequest request =
                AvailabilityRequest.builder()
                        .kost(kost)
                        .user(user)
                        .build();

        availabilityRepository.save(request);

        return AvailabilityMapper.toResponse(request);
    }

}