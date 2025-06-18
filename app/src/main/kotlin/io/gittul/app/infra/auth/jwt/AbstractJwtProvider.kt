package io.gittul.app.infra.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.gittul.app.infra.auth.dto.TokenUserInfo
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

abstract class AbstractJwtProvider(
    private val objectMapper: ObjectMapper
) {
    protected abstract val secretKey: String
    protected abstract val expirationTime: Long

    protected val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    fun createToken(userInfo: TokenUserInfo): String {
        val now = Date()
        val expiration = Date(now.time + expirationTime)
        val payload = objectMapper.writeValueAsString(userInfo)

        return Jwts.builder()
            .claim("userInfo", payload)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateToken(token: String): TokenUserInfo {
        try {
            return parseToken(token)
        } catch (e: Exception) {
            if (e is ExpiredJwtException) throw AuthenticationException.EXPIRED_TOKEN
            throw AuthenticationException.INVALID_TOKEN
        }
    }

    private fun parseToken(token: String): TokenUserInfo {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        val userInfoJson = claims.get("userInfo", String::class.java)
        return objectMapper.readValue(userInfoJson, TokenUserInfo::class.java)
    }

    fun getMaxAge(): Long = expirationTime / 1000 // Convert milliseconds to seconds
}
