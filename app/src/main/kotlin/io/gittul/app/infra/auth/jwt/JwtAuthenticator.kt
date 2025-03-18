package io.gittul.app.infra.auth.jwt

import io.gittul.app.domain.user.UserInquiryService
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.core.domain.user.entity.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class JwtAuthenticator(
    private val jwtProvider: JwtProvider,
    private val userService: UserInquiryService // Todo. ThreadLocal 로 변경 고려
) {

    fun authenticate(request: HttpServletRequest): User {
        val authorizationHeader: String =
            request.getHeader("Authorization") ?: throw AuthenticationException.TOKEN_NOT_FOUND

        val token: String = extractToken(authorizationHeader)

        val tokenUserInfo = jwtProvider.validateToken(token)
        return userService.getUserFromTokenInfo(tokenUserInfo)
    }

    private fun extractToken(header: String): String {
        if (!header.startsWith("Bearer ")) throw AuthenticationException.INVALID_GRANT_TYPE

        return header.substring(7)
    }
}
