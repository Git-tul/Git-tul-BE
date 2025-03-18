package io.gittul.app.infra.auth.jwt

import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.core.domain.user.entity.Role
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider {
    @Value("\${jwt.secret}")
    private val secretKey: String? = null

    @Value("\${jwt.expiration}")
    private val expirationTime: Long = 0


    /**
     * JWT 토큰 생성
     */
    fun createToken(userInfo: TokenUserInfo): String? {
        val now = Date()
        val accessExpiration = Date(now.time + this.expirationTime)

        return Jwts.builder()
            .subject(userInfo.userName)
            .claim("userId", userInfo.userId)
            .claim("email", userInfo.email)
            .claim("role", userInfo.role)
            .issuedAt(now)
            .expiration(accessExpiration)
            .signWith<SecretKey>(this.key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateToken(token: String?): TokenUserInfo {
        try {
            return parseToken(token)
        } catch (e: Exception) {
            if (e is ExpiredJwtException) {
                throw AuthenticationException.EXPIRED_TOKEN
            }
            throw AuthenticationException.INVALID_TOKEN
        }
    }

    private fun parseToken(token: String?): TokenUserInfo {
        val claims = Jwts.parser()
            .verifyWith(this.key)
            .build()
            .parseSignedClaims(token)
            .getPayload()

        return TokenUserInfo(
            claims.get<Long?>("userId", Long::class.java),
            claims.subject,  // userName
            claims.get<String?>("email", String::class.java),
            Role.valueOf(claims.get<String?>("role", String::class.java))
        )
    }

    private val key: SecretKey
        get() = Keys.hmacShaKeyFor(secretKey!!.toByteArray())
}
