package com.bandverse.bandverse_backend.util.response_builders.success;

import com.bandverse.bandverse_backend.business.auth.dto.LoginResponse;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import com.bandverse.bandverse_backend.util.response_builders.BaseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SuccessResponseBuilder {

    private final BaseSuccess baseSuccess;

    public RegisterUserResponse registerUser(
            UUID userId,
            RegistrationType registrationType,
            AccountStatus accountStatus
    ) {

        BaseResponse baseResponse =
                baseSuccess.baseSuccessResponse(
                        "User registered successfully"
                );

        return RegisterUserResponse.builder()
                .httpStatus(baseResponse.getHttpStatus())
                .status(baseResponse.getStatus())
                .responseCode(baseResponse.getResponseCode())
                .responseDescription(
                        baseResponse.getResponseDescription()
                )
                .userId(userId)
                .registrationType(registrationType)
                .accountStatus(accountStatus)
                .build();
    }

    public LoginResponse loginSuccess(String accessToken) {

        BaseResponse baseResponse =
                baseSuccess.baseSuccessResponse(
                        "Login successful"
                );

        return LoginResponse.builder()
                .httpStatus(baseResponse.getHttpStatus())
                .status(baseResponse.getStatus())
                .responseCode(baseResponse.getResponseCode())
                .responseDescription(
                        baseResponse.getResponseDescription()
                )
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
