package com.kost.kostapi;

import com.kost.kostapi.dto.auth.RegisterRequest;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.util.UUID;
import com.kost.kostapi.dto.auth.LoginRequest;
import com.kost.kostapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private record LoginResult(
            String email,
            String token) {
    }

    private User createRegularUser(String email) {

        User user = User.builder()
                .name("Regular")
                .email(email)
                .password(passwordEncoder.encode("password"))
                .role(UserRole.REGULAR)
                .credit(20)
                .build();

        return userRepository.save(user);
    }

    private LoginResult login() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        createRegularUser(email);

        LoginRequest request = new LoginRequest(
                email,
                "password");

        String json = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(json)
                .get("token")
                .asText();

        return new LoginResult(
                email,
                token);
    }

    @Test
    void register_success() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        RegisterRequest request = new RegisterRequest(
                "Regular User",
                email,
                "password",
                UserRole.REGULAR);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value(email))
                .andExpect(jsonPath("$.user.credit").value(20));
    }

    @Test
    void login_success() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        createRegularUser(email);

        LoginRequest request = new LoginRequest(
                email,
                "password");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email")
                        .value(email));
    }

    @Test
    void me_success() throws Exception {

        LoginResult login = login();

        mockMvc.perform(

                get("/api/auth/me")
                        .header(
                                "Authorization",
                                "Bearer " + login.token())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value(login.email()));

    }

    @Test
    void login_wrong_password() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        createRegularUser(email);

        LoginRequest request = new LoginRequest(
                email,
                "wrongpassword");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_email_not_found() throws Exception {

        LoginRequest request = new LoginRequest(
                "notfound@test.com",
                "password");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void me_without_token() throws Exception {

        mockMvc.perform(
                get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_invalid_token() throws Exception {

    mockMvc.perform(
    get("/api/auth/me")
    .header(
    "Authorization",
    "Bearer invalid-token"))
    .andExpect(status().isUnauthorized());
    }

    @Test
    void premium_register_gets_40_credit() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        RegisterRequest request = new RegisterRequest(
                "Premium",
                email,
                "password",
                UserRole.PREMIUM);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.credit")
                        .value(40));
    }

    @Test
    void owner_register_gets_zero_credit() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        RegisterRequest request = new RegisterRequest(
                "Owner",
                email,
                "password",
                UserRole.OWNER);
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.credit")
                        .value(0));
    }

    @Test
    void duplicate_email_should_fail() throws Exception {

        String email = UUID.randomUUID() + "@test.com";
        RegisterRequest request = new RegisterRequest(
                "User",
                email,
                "password",
                UserRole.REGULAR);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)));

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}