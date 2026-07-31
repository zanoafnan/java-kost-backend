package com.kost.kostapi;

import com.kost.kostapi.dto.kost.CreateKostRequest;
import com.kost.kostapi.dto.kost.UpdateKostRequest;
import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.repository.KostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class KostControllerTest extends BaseIntegrationTest {

    @Autowired
    private KostRepository kostRepository;

    private CreateKostRequest createRequest() {
        return new CreateKostRequest(
                "Kost Mawar",
                "AC Wifi",
                "Jakarta",
                BigDecimal.valueOf(1_500_000)
        );
    }

    private UpdateKostRequest updateRequest() {
        return new UpdateKostRequest(
                "Updated",
                "New Desc",
                "Bandung",
                BigDecimal.valueOf(2_000_000)
        );
    }

    private Kost createKost(User owner) {

        Kost kost = Kost.builder()
                .name("Kost Mawar")
                .description("AC Wifi")
                .location("Jakarta")
                .price(BigDecimal.valueOf(1_500_000))
                .owner(owner)
                .build();

        return kostRepository.save(kost);
    }

    @Test
    void owner_can_create_kost() throws Exception {

        mockMvc.perform(
                post("/api/kosts")
                        .header("Authorization", "Bearer " + ownerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Kost Mawar"))
                .andExpect(jsonPath("$.location").value("Jakarta"));
    }

    @Test
    void regular_cannot_create_kost() throws Exception {

        mockMvc.perform(
                post("/api/kosts")
                        .header("Authorization", "Bearer " + regularToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest()))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void premium_cannot_create_kost() throws Exception {

        mockMvc.perform(
                post("/api/kosts")
                        .header("Authorization", "Bearer " + premiumToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest()))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void guest_cannot_create_kost() throws Exception {

        mockMvc.perform(
                post("/api/kosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest()))
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void owner_can_update_own_kost() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        mockMvc.perform(
                put("/api/kosts/" + kost.getId())
                        .header("Authorization", "Bearer " + login(owner))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.location").value("Bandung"));
    }

    @Test
    void owner_cannot_update_other_owner_kost() throws Exception {

        User owner1 = createOwner();
        User owner2 = createOwner();

        Kost kost = createKost(owner1);

        mockMvc.perform(
                put("/api/kosts/" + kost.getId())
                        .header("Authorization", "Bearer " + login(owner2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest()))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void owner_can_delete_own_kost() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        mockMvc.perform(
                delete("/api/kosts/" + kost.getId())
                        .header("Authorization", "Bearer " + login(owner))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void owner_cannot_delete_other_owner_kost() throws Exception {

        User owner1 = createOwner();
        User owner2 = createOwner();

        Kost kost = createKost(owner1);

        mockMvc.perform(
                delete("/api/kosts/" + kost.getId())
                        .header("Authorization", "Bearer " + login(owner2))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void search_kost() throws Exception {

        User owner = createOwner();
        createKost(owner);

        mockMvc.perform(
                get("/api/kosts")
                        .param("name", "Mawar")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void detail_kost() throws Exception {

        User owner = createOwner();
        Kost kost = createKost(owner);

        mockMvc.perform(
                get("/api/kosts/" + kost.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kost Mawar"))
                .andExpect(jsonPath("$.location").value("Jakarta"));
    }
}