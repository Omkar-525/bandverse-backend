package com.bandverse.bandverse_backend.infra.exception;

import com.bandverse.bandverse_backend.infra.model.BaseResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        baseResponse.setStatus("Failure");
        baseResponse.setResponseCode("UNAUTHORIZED");
        baseResponse.setResponseDescription(
                "Authentication is required"
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        objectMapper.writeValue(
                response.getOutputStream(),
                baseResponse
        );
    }
}