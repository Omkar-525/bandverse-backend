package com.bandverse.bandverse_backend.business.user.dto;

import com.bandverse.bandverse_backend.infra.model.BaseRequest;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest extends BaseRequest {

    @NotBlank
    @Email
    private String email;

    private String phone;

    @NotNull
    private RegistrationType registrationType;
}
