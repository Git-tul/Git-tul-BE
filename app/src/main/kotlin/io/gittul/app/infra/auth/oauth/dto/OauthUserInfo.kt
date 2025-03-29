package io.gittul.app.infra.auth.oauth.dto

import jakarta.validation.constraints.Email

data class OauthUserInfo(
    val oauthId: String,
    val name: String,
    val email: String?,
    val profileImageUrl: String?
)
