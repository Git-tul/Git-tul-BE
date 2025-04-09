package io.gittul.app.domain.user

import io.gittul.app.domain.follow.FollowService
import io.gittul.app.domain.user.dto.UserDetailResponse
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import org.springframework.web.bind.annotation.*

@AccessGuard
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val followService: FollowService
) {

    @PostMapping("/follow")
    fun follow(
        @RequestBody followeeId: Long
    ) {
        val user = AuthContext.getUser();
        followService.follow(user, followeeId)
    }

    @PostMapping("/unfollow")
    fun unfollow(
        @RequestBody followeeId: Long
    ) {
        val user = AuthContext.getUser();
        followService.unfollow(user, followeeId)
    }

    @PostMapping("interest")
    fun interest(
        @RequestBody tags: List<String>
    ) {
        val user = AuthContext.getUser();
        userService.interest(user, tags)
    }

    @PostMapping("uninterest")
    fun uninterested(
        @RequestBody tag: String
    ) {
        val user = AuthContext.getUser();
        userService.unInterest(user, tag)
    }

    @GetMapping("/me")
    fun me(): UserDetailResponse {
        val user = AuthContext.getUser();
        return UserDetailResponse.of(user)
    }
}
