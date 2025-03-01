package io.gittul.infra.auth.exception;

import io.gittul.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends CustomException {
    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED, "Authentication Error");
    }

    public static AuthenticationException INVALID_TOKEN = new AuthenticationException("유효하지 않은 토큰입니다");
    public static AuthenticationException TOKEN_NOT_FOUND = new AuthenticationException("토큰이 없습니다");
    public static AuthenticationException INVALID_GRANT_TYPE = new AuthenticationException("잘못된 인증 방식입니다");
    public static AuthenticationException EXPIRED_TOKEN = new AuthenticationException("만료된 토큰입니다");

    public static AuthenticationException USER_NOT_FOUND = new AuthenticationException("존재하지 않는 사용자입니다");
    public static AuthenticationException WRONG_PASSWORD = new AuthenticationException("비밀번호가 일치하지 않습니다");
}
