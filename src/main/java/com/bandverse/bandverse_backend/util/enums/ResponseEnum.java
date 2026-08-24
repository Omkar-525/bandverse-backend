package com.bandverse.bandverse_backend.util.enums;

public enum ResponseEnum {

    SUCCESS("200", "Success"),
    FAILURE("400", "Failure");

    private final String code;
    private final String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
