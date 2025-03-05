package io.gittul.domain.user;

import io.gittul.domain.follow.FollowService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final FollowService followService;

    @PostMapping("/follow")
    public void follow(@Authenticated User user, @RequestBody Long followeeId) {
        followService.follow(user, followeeId);
    }

    @PostMapping("/unfollow")
    public void unfollow(@Authenticated User user, @RequestBody Long followeeId) {
        followService.unfollow(user, followeeId);
    }
}
