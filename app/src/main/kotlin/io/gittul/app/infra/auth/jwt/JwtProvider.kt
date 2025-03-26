package io.gittul.app.infra.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    private val objectMapper: ObjectMapper = ObjectMapper()
) {
    @Value("\${jwt.secret}")
    private val secretKey: String? = null

    @Value("\${jwt.expiration}")
    private val expirationTime: Long = 0


    /**
     * JWT 토큰 생성
     */
    fun createToken(userInfo: TokenUserInfo): String {
        val now = Date()
        val accessExpiration = Date(now.time + this.expirationTime)

        val userInfoJson: String = objectMapper.writeValueAsString(userInfo)

        return Jwts.builder()
            .claim("userInfo", userInfoJson)
            .issuedAt(now)
            .expiration(accessExpiration)
            .signWith(this.key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateToken(token: String): TokenUserInfo {
        try {
            return parseToken(token)
        } catch (e: Exception) {
            println(e)
            if (e is ExpiredJwtException) {
                throw AuthenticationException.EXPIRED_TOKEN
            }
            throw AuthenticationException.INVALID_TOKEN
        }
    }

    private fun parseToken(token: String): TokenUserInfo {
        val claims = Jwts.parser()
            .verifyWith(this.key)
            .build()
            .parseSignedClaims(token)
            .payload

        val userInfoJson: String = claims.get("userInfo", String::class.java)
        return objectMapper.readValue(userInfoJson, TokenUserInfo::class.java)
    }

    private val key: SecretKey
        get() = Keys.hmacShaKeyFor(secretKey!!.toByteArray())
}
