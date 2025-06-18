package io.gittul.app.infra.auth.dto

import io.gittul.core.domain.user.entity.Role
import io.gittul.core.domain.user.entity.User

data class TokenUserInfo(
    val userId: Long,
    val userName: String,
    val email: String,
    val role: Role
) {
    companion object {
        fun of(user: User): TokenUserInfo {
            return TokenUserInfo(
                user.userId,
                user.userName,
                user.email,
                user.role
            )
        }
    }
}
