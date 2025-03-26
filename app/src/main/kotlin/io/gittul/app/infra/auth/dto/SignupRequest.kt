package io.gittul.app.infra.auth.dto

import io.gittul.core.domain.user.entity.Password.ValidPassword
import io.gittul.core.global.validation.OptionalURL
import jakarta.validation.constraints.Email

data class SignupRequest(
    val userName: String,

    @field:Email
    val email: String,

    @field:ValidPassword
    val password: String,

    @field:OptionalURL
    val profileImage: String
)
