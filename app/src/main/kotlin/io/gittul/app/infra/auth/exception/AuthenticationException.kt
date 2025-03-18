package io.gittul.app.infra.auth.exception

import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus

class AuthenticationException : CustomException {
    constructor(message: String?) : super(HttpStatus.UNAUTHORIZED, message)

    constructor() : super(HttpStatus.UNAUTHORIZED, "Authentication Error")

    companion object {
        var INVALID_TOKEN: AuthenticationException = AuthenticationException("유효하지 않은 토큰입니다")
        var TOKEN_NOT_FOUND: AuthenticationException = AuthenticationException("토큰이 없습니다")
        var INVALID_GRANT_TYPE: AuthenticationException = AuthenticationException("잘못된 인증 방식입니다")
        var EXPIRED_TOKEN: AuthenticationException = AuthenticationException("만료된 토큰입니다")
        var USER_NOT_FOUND: AuthenticationException = AuthenticationException("존재하지 않는 사용자입니다")
        var WRONG_PASSWORD: AuthenticationException = AuthenticationException("비밀번호가 일치하지 않습니다")
    }
}
