package com.pragma.bootcamps.report.domain.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    CAPABILITY_NOT_FOUND("Some capability IDs do not exist."),
    BOOTCAMP_NOT_FOUND("Bootcamp not found with ID: %s"),
    BOOTCAMP_ALREADY_EXISTS("A bootcamp with the name '%s' already exists."),
    WEB_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in the capability microservice.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
