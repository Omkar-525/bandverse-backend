package com.bandverse.bandverse_backend.util.response_builders;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import com.bandverse.bandverse_backend.util.enums.ResponseEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BaseSuccess {

    public BaseResponse baseSuccessResponse(String description) {

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setHttpStatus(HttpStatus.OK);
        baseResponse.setResponseCode(ResponseEnum.SUCCESS.getCode());
        baseResponse.setStatus(ResponseEnum.SUCCESS.getMessage());
        baseResponse.setResponseDescription(description);

        return baseResponse;
    }
}