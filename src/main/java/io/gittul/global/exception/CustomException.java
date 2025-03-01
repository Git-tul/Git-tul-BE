package io.gittul.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private Throwable cause;

    public CustomException(String message) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public CustomException(String message, Throwable cause) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.cause = cause;
    }

    public static CustomException ofDefault() {
        return new CustomException("Internal Server Error");
    }
}
