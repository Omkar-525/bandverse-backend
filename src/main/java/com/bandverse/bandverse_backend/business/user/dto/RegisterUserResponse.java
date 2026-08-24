package com.bandverse.bandverse_backend.business.user.dto;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse extends BaseResponse {

    private UUID userId;

    private RegistrationType registrationType;

    private AccountStatus accountStatus;

    @Builder
    public RegisterUserResponse(
            HttpStatus httpStatus,
            String status,
            String responseCode,
            String responseDescription,
            UUID userId,
            RegistrationType registrationType,
            AccountStatus accountStatus
    ) {
        super(
                httpStatus,
                status,
                responseCode,
                responseDescription
        );

        this.userId = userId;
        this.registrationType = registrationType;
        this.accountStatus = accountStatus;
    }
}
