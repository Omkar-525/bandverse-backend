package com.bandverse.bandverse_backend.business.auth.service;

import com.bandverse.bandverse_backend.business.auth.dto.LoginRequest;
import com.bandverse.bandverse_backend.business.auth.dto.LoginResponse;
import com.bandverse.bandverse_backend.security.JwtService;
import com.bandverse.bandverse_backend.util.response_builders.success.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SuccessResponseBuilder successResponseBuilder;

    @Override
    public LoginResponse login(LoginRequest request) {

        log.info(
                "User login attempt. email={}",
                request.getEmail()
        );

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String accessToken =
                jwtService.generateToken(authentication);

        log.info(
                "User login successful. email={}",
                request.getEmail()
        );

        return successResponseBuilder.loginSuccess(
                accessToken
        );
    }
}