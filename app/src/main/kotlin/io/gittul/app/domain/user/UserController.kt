package io.gittul.app.domain.user

import io.gittul.app.domain.follow.FollowService
import io.gittul.app.domain.user.dto.UserDetailResponse
import io.gittul.app.infra.auth.aop.Authenticated
import io.gittul.core.domain.user.entity.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val followService: FollowService
) {

    @PostMapping("/follow")
    fun follow(
        @Authenticated user: User,
        @RequestBody followeeId: Long
    ) {
        followService.follow(user, followeeId)
    }

    @PostMapping("/unfollow")
    fun unfollow(
        @Authenticated user: User,
        @RequestBody followeeId: Long
    ) {
        followService.unfollow(user, followeeId)
    }

    @PostMapping("interest")
    fun interest(
        @Authenticated user: User,
        @RequestBody tags: List<String>
    ) {
        userService.interest(user, tags)
    }

    @PostMapping("uninterest")
    fun uninterested(
        @Authenticated user: User,
        @RequestBody tag: String
    ) {
        userService.unInterest(user, tag)
    }

    @GetMapping("/me")
    fun me(@Authenticated user: User): UserDetailResponse {
        return UserDetailResponse.of(user)
    }
}
