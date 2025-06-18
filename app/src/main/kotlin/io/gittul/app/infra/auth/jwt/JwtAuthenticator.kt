package io.gittul.app.infra.auth.jwt

import io.gittul.app.domain.user.UserInquiryService
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.core.domain.user.entity.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class JwtAuthenticator(
    private val jwtProvider: JwtProvider,
    private val userService: UserInquiryService,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider
) {

    fun authenticate(request: HttpServletRequest): User {
        val token = extractToken(request) ?: throw AuthenticationException.INVALID_TOKEN
        return accessTokenProvider.validateToken(token)
    }

    fun extractRefreshToken(request: HttpServletRequest): String? {
        return request.cookies?.find { it.name == "refresh_token" }?.value
    }

    private fun extractToken(request: HttpServletRequest): String? {
        // 1. 쿠키에서 토큰 확인
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "access_token") {
                    return cookie.value
                }
            }
        }

        // 2. Authorization 헤더에서 토큰 확인
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7)
        }

        return null
    }
}
