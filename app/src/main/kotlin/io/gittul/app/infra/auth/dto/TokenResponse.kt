package io.gittul.app.infra.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String? = null
)
