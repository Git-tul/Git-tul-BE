package io.gittul.infra.auth.jwt;

import io.gittul.domain.user.entity.Role;
import io.gittul.domain.user.entity.User;

public record TokenUserInfo(
        Long userId,
        String userName,
        String email,
        Role role
) {

    public static TokenUserInfo of(User user) {
        return new TokenUserInfo(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
