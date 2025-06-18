package io.gittul.app.infra.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RefreshTokenProvider(
    objectMapper: ObjectMapper,
    @Value("\${jwt.refresh.secret}") override val secretKey: String,
    @Value("\${jwt.refresh.expiration}") override val expirationTime: Long
) : AbstractJwtProvider(objectMapper)
