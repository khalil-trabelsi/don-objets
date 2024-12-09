package com.m2.exception;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.Getter;

public class EntityNotFoundException extends RuntimeException{
    @Getter
    private ErrorCodes errorCodes;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCodes = errorCode;
    }

    public EntityNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCodes = errorCode;
    }

}
