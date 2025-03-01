package io.gittul.domain.user.exception;

import io.gittul.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserException extends CustomException {

    public UserException(String message, HttpStatus status) {
        super(status, message);
    }

    public UserException(HttpStatus status, String message) {
        super(status, message);
    }

    public UserException() {
        super(HttpStatus.BAD_REQUEST, "User Error");
    }

    public static final UserException USERNAME_ALREADY_EXISTS =
            new UserException(HttpStatus.CONFLICT, "이미 사용 중인 이름입니다.");

    public static final UserException EMAIL_ALREADY_EXISTS =
            new UserException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");

    public static final UserException USER_NOT_FOUND =
            new UserException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
}
