package com.bandverse.bandverse_backend.business.auth.service;

import com.bandverse.bandverse_backend.business.auth.dto.LoginRequest;
import com.bandverse.bandverse_backend.business.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}