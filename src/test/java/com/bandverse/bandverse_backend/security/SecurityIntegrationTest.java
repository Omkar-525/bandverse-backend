package com.bandverse.bandverse_backend.security;

import com.bandverse.bandverse_backend.business.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import com.bandverse.bandverse_backend.business.user.entity.User;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import static org.assertj.core.api.Assertions.assertThat;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.AfterEach;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @NotBlank
    private String password;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void protectedEndpointWithoutTokenReturnsUnauthorized()
            throws Exception {
        mockMvc.perform(get("/api/v1/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void validJwtAllowsAccessToProtectedEndpoint() throws Exception {

        Authentication authentication = new UsernamePasswordAuthenticationToken("security-test@bandverse.test", null, java.util.Collections.emptyList());

        String token = jwtService.generateToken(authentication);

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization","Bearer " + token))
                .andExpect(status().isOk()).andExpect(content().string("security-test@bandverse.test"));
    }

    @Test
    void tamperedJwtIsRejected() throws Exception {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("security-test@bandverse.test", null, java.util.Collections.emptyList());

        String token = jwtService.generateToken(authentication);

        String[] tokenParts = token.split("\\.");

        String tamperedPayload = tokenParts[1].substring(0, tokenParts[1].length() - 1) + "A";

        String tamperedToken = tokenParts[0] + "." + tamperedPayload + "." + tokenParts[2];

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + tamperedToken)).andExpect(status().isUnauthorized());
    }

    @Test
    void expiredJwtIsRejected() throws Exception {

        Instant issuedAt = Instant.now().minusSeconds(120);
        Instant expiresAt = Instant.now().minusSeconds(60);

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("bandverse-test").subject("security-test@bandverse.test")
                .issuedAt(issuedAt).expiresAt(expiresAt)
                .id(UUID.randomUUID().toString()).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + token)).andExpect(status().isUnauthorized());
    }

    @Test
    void validCredentialsReturnAccessToken() throws Exception {

        User user = new User();

        user.setEmail("login-test@bandverse.test");
        user.setPasswordHash(passwordEncoder.encode("CorrectPassword123!"));
        user.setDisplayName("Login Test");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setMfaEnabled(false);

        userRepository.save(user);

        String requestBody = """
            {"email": "login-test@bandverse.test","password": "CorrectPassword123!"}
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty()).andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void wrongPasswordReturnsUnauthorized() throws Exception {

        User user = new User();

        user.setEmail("wrong-password@bandverse.test");
        user.setPasswordHash(passwordEncoder.encode("CorrectPassword123!"));
        user.setDisplayName("Wrong Password Test");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setMfaEnabled(false);

        userRepository.save(user);

        String requestBody = """
            {"email": "wrong-password@bandverse.test","password": "WrongPassword123!" }
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    void unknownEmailReturnsUnauthorized() throws Exception {

        String requestBody = """
            {"email": "does-not-exist@bandverse.test","password": "CorrectPassword123!"}
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    void deactivatedAccountReturnsUnauthorized() throws Exception {

        User user = new User();

        user.setEmail("deactivated@bandverse.test");
        user.setPasswordHash(
                passwordEncoder.encode("CorrectPassword123!")
        );
        user.setDisplayName("Deactivated Test");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.DEACTIVATED);
        user.setMfaEnabled(false);

        userRepository.saveAndFlush(user);

        String requestBody = """
            {"email": "deactivated@bandverse.test","password": "CorrectPassword123!"}
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    void suspendedAccountReturnsUnauthorized() throws Exception {

        User user = new User();

        user.setEmail("suspended@bandverse.test");
        user.setPasswordHash(passwordEncoder.encode("CorrectPassword123!"));
        user.setDisplayName("Suspended Test");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.SUSPENDED);
        user.setMfaEnabled(false);

        userRepository.saveAndFlush(user);

        String requestBody = """
            { "email": "suspended@bandverse.test","password": "CorrectPassword123!"}
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    void deletedAccountReturnsUnauthorized() throws Exception {

        User user = new User();

        user.setEmail("deleted@bandverse.test");
        user.setPasswordHash(passwordEncoder.encode("CorrectPassword123!"));
        user.setDisplayName("Deleted Test");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setDeletedAt(OffsetDateTime.now());
        user.setMfaEnabled(false);

        userRepository.saveAndFlush(user);

        String requestBody = """
            {"email": "deleted@bandverse.test", "password": "CorrectPassword123!" }
            """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    void successfulUserRegistrationReturnsCreatedUser() throws Exception {

        String requestBody = """
            {"email": "registration-test@bandverse.test","password": "CorrectPassword123!","phone": "+919999999999","registrationType": "USER","displayName": "Registration Test" }
            """;

        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.registrationType").value("USER"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {

        User user = new User();

        user.setEmail("duplicate@bandverse.test");
        user.setPasswordHash(
                passwordEncoder.encode("CorrectPassword123!")
        );
        user.setDisplayName("Existing User");
        user.setRegistrationType(RegistrationType.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setMfaEnabled(false);

        userRepository.saveAndFlush(user);

        String requestBody = """
            {"email": "duplicate@bandverse.test","password": "AnotherPassword123!", "phone": "+919888888888","registrationType": "USER","displayName": "Duplicate User"}
            """;

        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isConflict());
    }

    @Test
    void invalidEmailReturnsBadRequest() throws Exception {

        String requestBody = """
            {"email": "not-an-email","password": "CorrectPassword123!","phone": "+919999999999","registrationType": "USER","displayName": "Validation Test"}
            """;

        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void blankPasswordReturnsBadRequest() throws Exception {

        String requestBody = """
            {"email": "blank-password@bandverse.test","password": "","phone": "+919999999999","registrationType": "USER","displayName": "Validation Test"}
            """;

        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void blankDisplayNameReturnsBadRequest() throws Exception {

        String requestBody = """
                {"email": "blank-display-name@bandverse.test","password": "CorrectPassword123!","phone": "+919999999999","registrationType": "USER", "displayName": ""}
                """;
        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }
    @Test
    void missingRegistrationTypeReturnsBadRequest() throws Exception {

        String requestBody = """
            {"email": "missing-type@bandverse.test","password": "CorrectPassword123!","phone": "+919999999999","displayName": "Validation Test" }
            """;
        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void registrationStoresPasswordAsHash() throws Exception {

        String rawPassword = "CorrectPassword123!";

        String requestBody = """
            {"email": "password-hash@bandverse.test","password": "%s","phone": "+919999999999","registrationType": "USER","displayName": "Password Hash Test"}
            """.formatted(rawPassword);

        mockMvc.perform(post("/api/v1/users/register").contentType(APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());

        User savedUser = userRepository.findByEmail("password-hash@bandverse.test").orElseThrow();
        assertThat(savedUser.getPasswordHash()).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, savedUser.getPasswordHash())).isTrue();
    }
}