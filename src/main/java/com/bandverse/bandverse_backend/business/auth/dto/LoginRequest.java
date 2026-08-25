package com.bandverse.bandverse_backend.business.auth.dto;

import com.bandverse.bandverse_backend.infra.model.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest extends BaseRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}