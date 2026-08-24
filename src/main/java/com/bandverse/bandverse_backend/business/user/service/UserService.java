package com.bandverse.bandverse_backend.business.user.service;

import com.bandverse.bandverse_backend.business.user.dto.RegisterUserRequest;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;

public interface UserService {

    RegisterUserResponse register(RegisterUserRequest request);
}
