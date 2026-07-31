package com.kost.kostapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kost.kostapi.dto.auth.LoginRequest;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.CreditAmount;
import com.kost.kostapi.enums.UserRole;
import com.kost.kostapi.repository.KostRepository;
import com.kost.kostapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected KostRepository kostRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    protected User createOwner() {
        return createUser(UserRole.OWNER);
    }

    protected User createRegular() {
        return createUser(UserRole.REGULAR);
    }

    protected User createPremium() {
        return createUser(UserRole.PREMIUM);
    }

    protected User createUser(UserRole role) {

        int credit = switch (role) {
            case OWNER -> CreditAmount.OWNER.getValue();
            case REGULAR -> CreditAmount.REGULAR.getValue();
            case PREMIUM -> CreditAmount.PREMIUM.getValue();
        };

        User user = User.builder()
                .name(role.name())
                .email(UUID.randomUUID() + "@test.com")
                .password(passwordEncoder.encode("password"))
                .role(role)
                .credit(credit)
                .build();

        return userRepository.save(user);
    }

    protected String login(User user) throws Exception {

        LoginRequest request = new LoginRequest(
                user.getEmail(),
                "password");

        String json = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode node = objectMapper.readTree(json);

        return node.get("token").asText();
    }

    protected String ownerToken() throws Exception {
        return login(createOwner());
    }

    protected String regularToken() throws Exception {
        return login(createRegular());
    }

    protected String premiumToken() throws Exception {
        return login(createPremium());
    }
}