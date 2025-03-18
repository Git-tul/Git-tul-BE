package io.gittul.app.domain.user.dto

import io.gittul.core.domain.user.entity.User

data class UserProfileResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val profileImage: String?
) {
    companion object {
        fun of(user: User): UserProfileResponse {
            return UserProfileResponse(
                user.userId,
                user.userName,
                user.email,
                user.profileImage
            )
        }
    }
}
