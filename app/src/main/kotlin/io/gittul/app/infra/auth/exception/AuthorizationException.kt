package io.gittul.app.infra.auth.exception

import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus

class AuthorizationException : CustomException {
    constructor(message: String?) : super(HttpStatus.FORBIDDEN, message)

    constructor() : super(HttpStatus.FORBIDDEN, "Authorization Error")

    companion object {
        var ADMIN_ONLY: AuthorizationException = AuthorizationException("관리자 권한이 필요합니다.")
    }
}
