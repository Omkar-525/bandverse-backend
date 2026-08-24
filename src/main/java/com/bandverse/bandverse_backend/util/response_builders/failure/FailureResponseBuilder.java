package com.bandverse.bandverse_backend.util.response_builders.failure;

import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import com.bandverse.bandverse_backend.util.response_builders.BaseFailure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FailureResponseBuilder {

    private final BaseFailure baseFailure;

    public RegisterUserResponse registerUser(
            HttpStatus httpStatus,
            String responseCode,
            String description
    ) {

        BaseResponse baseResponse =
                baseFailure.baseFailureResponse(
                        httpStatus,
                        responseCode,
                        description
                );

        return RegisterUserResponse.builder()
                .httpStatus(baseResponse.getHttpStatus())
                .status(baseResponse.getStatus())
                .responseCode(baseResponse.getResponseCode())
                .responseDescription(
                        baseResponse.getResponseDescription()
                )
                .build();
    }
}
