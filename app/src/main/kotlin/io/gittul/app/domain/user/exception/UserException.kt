package io.gittul.app.domain.user.exception

import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus

class UserException : CustomException {
    constructor(message: String?, status: HttpStatus?) : super(status, message)

    constructor(status: HttpStatus?, message: String?) : super(status, message)

    constructor() : super(HttpStatus.BAD_REQUEST, "User Error")

    companion object {
        val USERNAME_ALREADY_EXISTS: UserException = UserException(HttpStatus.CONFLICT, "이미 사용 중인 이름입니다.")
        val EMAIL_ALREADY_EXISTS: UserException = UserException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.")
        val USER_NOT_FOUND: UserException = UserException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.")
    }
}
