package io.gittul.infra.auth.exception;

import io.gittul.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AuthorizationException extends CustomException {
    public AuthorizationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public AuthorizationException() {
        super(HttpStatus.FORBIDDEN, "Authorization Error");
    }

    public static AuthorizationException ADMIN_ONLY = new AuthorizationException("관리자 권한이 필요합니다.");
}
