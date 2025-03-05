package io.gittul.domain.user.dto;

import io.gittul.domain.user.entity.User;

public record UserProfileResponse(
        Long id,
        String nickname,
        String email,
        String profileImage
) {
    public static UserProfileResponse of(User user) {
        return new UserProfileResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileImage()
        );
    }
}
