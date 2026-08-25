package com.bandverse.bandverse_backend.infra.exception;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setHttpStatus(HttpStatus.FORBIDDEN);
        baseResponse.setStatus("Failure");
        baseResponse.setResponseCode("FORBIDDEN");
        baseResponse.setResponseDescription(
                "You do not have permission to access this resource"
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        objectMapper.writeValue(
                response.getOutputStream(),
                baseResponse
        );
    }
}