package io.gittul.app.domain.user.dto

import io.gittul.core.domain.user.entity.User

data class UserDetailResponse(
    val userId: Long,
    val nickname: String,
    val email: String,
    val profileImageUrl: String?,
    val followerCount: Int,
    val followingCount: Int,
    val followings: List<UserProfileResponse>,
    val likedCount: Int,
    val interests: List<String>
) {
    companion object {
        fun of(user: User): UserDetailResponse {
            val details = user.details

            val likeCount = details.threads.stream()
                .mapToInt { it.likes.size }
                .sum()

            return UserDetailResponse(
                user.userId,
                user.userName,
                user.email,
                user.profileImage,
                details.followers.size,
                details.followings.size,
                details.followings.stream()
                    .map<User> { it.followee }
                    .map<UserProfileResponse> { UserProfileResponse.of(it) }
                    .toList(),
                likeCount,
                details.interests.stream()
                    .map<String> { it.tag.tagName }
                    .toList()
            )
        }
    }
}
