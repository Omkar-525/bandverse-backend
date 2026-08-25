package com.bandverse.bandverse_backend.user;

import com.bandverse.bandverse_backend.business.user.controller.UserController;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserRequest;
import com.bandverse.bandverse_backend.infra.exception.GlobalExceptionHandler;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import com.bandverse.bandverse_backend.business.user.service.UserService;
import com.bandverse.bandverse_backend.infra.exception.UserAlreadyExistsException;
import com.bandverse.bandverse_backend.util.response_builders.BaseFailure;
import com.bandverse.bandverse_backend.util.response_builders.failure.FailureResponseBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({
        GlobalExceptionHandler.class,
        FailureResponseBuilder.class,
        BaseFailure.class
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();

        RegisterUserResponse response =
                RegisterUserResponse.builder()
                        .httpStatus(HttpStatus.CREATED)
                        .status("Success")
                        .responseCode("200")
                        .responseDescription(
                                "User registered successfully"
                        )
                        .userId(userId)
                        .registrationType(RegistrationType.USER)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(userService.register(any(RegisterUserRequest.class)))
                .thenReturn(response);

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("controller@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Controller Test User");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.USER);

        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.response_code").value("200"))
                .andExpect(
                        jsonPath("$.response_description")
                                .value("User registered successfully")
                )
                .andExpect(
                        jsonPath("$.userId")
                                .value(userId.toString())
                )
                .andExpect(
                        jsonPath("$.registrationType")
                                .value("USER")
                )
                .andExpect(
                        jsonPath("$.accountStatus")
                                .value("ACTIVE")
                );
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExists() throws Exception {

        when(userService.register(any(RegisterUserRequest.class)))
                .thenThrow(
                        new UserAlreadyExistsException(
                                "A user with this email already exists"
                        )
                );

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("existing@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Existing Test User");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.USER);

        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("Failure"))
                .andExpect(
                        jsonPath("$.response_code")
                                .value("USER_ALREADY_EXISTS")
                )
                .andExpect(
                        jsonPath("$.response_description")
                                .value(
                                        "A user with this email already exists"
                                )
                );
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("");
        request.setPhone("9999999999");
        request.setRegistrationType(null);

        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Failure"))
                .andExpect(
                        jsonPath("$.response_code")
                                .value("VALIDATION_ERROR")
                );
    }
}