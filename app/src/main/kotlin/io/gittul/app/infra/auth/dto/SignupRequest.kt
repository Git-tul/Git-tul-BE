package io.gittul.app.infra.auth.dto

import io.gittul.core.domain.user.entity.Password.ValidPassword
import jakarta.validation.constraints.Email

data class SignupRequest(
    val userName: String,

    @Email
    val email: String,

    @ValidPassword
    val password: String
)
