package com.kost.kostapi;

import com.kost.kostapi.dto.availability.CreateAvailabilityRequest;
import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvailabilityControllerTest extends BaseIntegrationTest {

    private Kost createKost(User owner) {

        return kostRepository.save(
                Kost.builder()
                        .name("Kost Mawar")
                        .description("Desc")
                        .location("Jakarta")
                        .price(BigDecimal.valueOf(1_000_000))
                        .owner(owner)
                        .build());
    }

    @Test
    void create_success_regular() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        User regular = createRegular();

        mockMvc.perform(
                post("/api/availability")
                        .header(
                                "Authorization",
                                "Bearer " + login(regular))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        kost.getId()))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.kostId").value(kost.getId()))
                .andExpect(jsonPath("$.userId").value(regular.getId()));

        User updated = userRepository
                .findById(regular.getId())
                .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                15,
                updated.getCredit());
    }

    @Test
    void create_success_premium() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        User premium = createPremium();

        mockMvc.perform(
                post("/api/availability")
                        .header(
                                "Authorization",
                                "Bearer " + login(premium))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        kost.getId()))))
                .andExpect(status().isCreated());

        User updated = userRepository
                .findById(premium.getId())
                .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                35,
                updated.getCredit());
    }

    @Test
    void owner_cannot_request_availability() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        mockMvc.perform(
                post("/api/availability")
                        .header(
                                "Authorization",
                                "Bearer " + login(owner))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        kost.getId()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void insufficient_credit_should_fail() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        User regular = createRegular();
        regular.setCredit(0);
        userRepository.save(regular);

        mockMvc.perform(
                post("/api/availability")
                        .header(
                                "Authorization",
                                "Bearer " + login(regular))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        kost.getId()))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void kost_not_found_should_fail() throws Exception {

        User regular = createRegular();

        mockMvc.perform(
                post("/api/availability")
                        .header(
                                "Authorization",
                                "Bearer " + login(regular))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        999999L))))
                .andExpect(status().isNotFound());
    }

    @Test
    void unauthenticated_should_fail() throws Exception {

        mockMvc.perform(
                post("/api/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAvailabilityRequest(
                                        1L))))
                .andExpect(status().isUnauthorized());
    }
}