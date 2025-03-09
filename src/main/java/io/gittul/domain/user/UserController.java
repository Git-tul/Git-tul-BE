package io.gittul.domain.user;

import io.gittul.domain.follow.FollowService;
import io.gittul.domain.user.dto.UserDetailResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/follow")
    public void follow(@Authenticated User user, @RequestBody Long followeeId) {
        followService.follow(user, followeeId);
    }

    @PostMapping("/unfollow")
    public void unfollow(@Authenticated User user, @RequestBody Long followeeId) {
        followService.unfollow(user, followeeId);
    }

    @PostMapping("interest")
    public void interest(@Authenticated User user, @RequestBody List<String> tags) {
        userService.interest(user, tags);
    }

    @PostMapping("uninterest")
    public void uninterested(@Authenticated User user, @RequestBody String tag) {
        userService.unInterest(user, tag);
    }

    @GetMapping("/me")
    public UserDetailResponse me(@Authenticated User user) {
        return UserDetailResponse.of(user);
    }
}
