package io.gittul.app.domain.follow

import io.gittul.app.domain.user.UserRepository
import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class FollowService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun follow(follower: User, followeeId: Long) {
        val followee = getUserOrElseThrow(followeeId)

        if (follower.userId == followeeId) {
            throw CustomException(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다.")
        }

        if (follower.isFollowing(followee)) {
            throw CustomException(HttpStatus.BAD_REQUEST, "이미 팔로우중인 유저입니다.")
        }

        follower.follow(followee)
        userRepository.save(follower)
    }

    @Transactional
    fun unfollow(follower: User, followeeId: Long) {
        val followee = getUserOrElseThrow(followeeId)

        if (!follower.isFollowing(followee)) {
            throw CustomException(HttpStatus.BAD_REQUEST, "팔로우중인 유저가 아닙니다.")
        }

        follower.unfollow(followee)
        userRepository.save(follower)
    }

    fun getFollowers(user: User): List<UserProfileResponse> {
        return user.details.followers.stream()
            .map<User> { it.follower }
            .map<UserProfileResponse> { UserProfileResponse.Companion.of(it) }
            .toList()
    }

    fun getFollowings(user: User): List<UserProfileResponse> {
        return user.details.followings.stream()
            .map<User> { it.followee }
            .map<UserProfileResponse> { UserProfileResponse.Companion.of(it) }
            .toList()
    }

    private fun getUserOrElseThrow(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow<CustomException>(Supplier { CustomException(HttpStatus.NOT_FOUND, "User not found") })
    }
}
