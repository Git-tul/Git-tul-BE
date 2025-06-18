package io.gittul.app.infra.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AccessTokenProvider(
    objectMapper: ObjectMapper,
    @Value("\${jwt.access.secret}") override val secretKey: String,
    @Value("\${jwt.access.expiration}") override val expirationTime: Long
) : AbstractJwtProvider(objectMapper)
