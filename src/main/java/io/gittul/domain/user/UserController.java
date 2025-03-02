package io.gittul.domain.user;

import io.gittul.infra.auth.aop.Authenticated;
import io.gittul.infra.auth.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("my")
    public TokenUserInfo getMyInfo(@Authenticated TokenUserInfo userInfo) {
        return userInfo;
    }
}
