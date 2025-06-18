package io.gittul.app.infra.auth.response

import io.gittul.app.infra.auth.dto.TokenResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AuthResponseHandler {
    fun handleResponse(
        token: TokenResponse,
        responseType: String,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        if (responseType.equals("cookie")) {
            response.addHeader("Set-Cookie", buildAccessTokenCookie(token.accessToken, token.accessTokenMaxAge).toString())
            response.addHeader("Set-Cookie", buildRefreshTokenCookie(token.refreshToken, token.refreshTokenMaxAge).toString())
            return ResponseEntity.ok().body(mapOf("message" to "쿠키로 전달됨"))
        }

        return ResponseEntity.ok(token)
    }

    private fun buildAccessTokenCookie(token: String, maxAge: Long): ResponseCookie {
        return ResponseCookie.from("access_token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(maxAge)
            .build()
    }

    private fun buildRefreshTokenCookie(token: String, maxAge: Long): ResponseCookie {
        return ResponseCookie.from("refresh_token", token)
            .httpOnly(true)
            .secure(true)
            .path("/auth/refresh")
            .sameSite("Strict")
            .maxAge(maxAge)
            .build()
    }
} 
