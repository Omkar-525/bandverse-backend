package com.bandverse.bandverse_backend.infra.exception;

import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.util.response_builders.failure.FailureResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final FailureResponseBuilder failureResponseBuilder;

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<RegisterUserResponse> handleUserAlreadyExists(
            UserAlreadyExistsException exception
    ) {

        log.warn(
                "User registration failed. errorCode=USER_ALREADY_EXISTS"
        );

        RegisterUserResponse response =
                failureResponseBuilder.registerUser(
                        HttpStatus.CONFLICT,
                        "USER_ALREADY_EXISTS",
                        exception.getMessage()
                );

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegisterUserResponse> handleValidationException(
            MethodArgumentNotValidException exception
    ) {

        String description = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));

        log.warn(
                "Request validation failed. errors={}",
                description
        );

        RegisterUserResponse response =
                failureResponseBuilder.registerUser(
                        HttpStatus.BAD_REQUEST,
                        "VALIDATION_ERROR",
                        description
                );

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
