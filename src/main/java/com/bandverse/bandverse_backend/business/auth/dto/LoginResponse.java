package com.bandverse.bandverse_backend.business.auth.dto;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends BaseResponse {

    private String accessToken;

    private String tokenType;

    @Builder
    public LoginResponse(
            HttpStatus httpStatus,
            String status,
            String responseCode,
            String responseDescription,
            String accessToken,
            String tokenType
    ) {
        super(
                httpStatus,
                status,
                responseCode,
                responseDescription
        );
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}