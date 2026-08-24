package com.bandverse.bandverse_backend.util.response_builders;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import com.bandverse.bandverse_backend.util.enums.ResponseEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BaseFailure {

    public BaseResponse baseFailureResponse(
            HttpStatus httpStatus,
            String responseCode,
            String description
    ) {

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setHttpStatus(httpStatus);
        baseResponse.setResponseCode(responseCode);
        baseResponse.setStatus(ResponseEnum.FAILURE.getMessage());
        baseResponse.setResponseDescription(description);

        return baseResponse;
    }
}
