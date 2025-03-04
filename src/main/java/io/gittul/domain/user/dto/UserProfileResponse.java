package io.gittul.domain.user.dto;

import io.gittul.domain.user.entity.User;

public record UserProfileResponse(
        String nickname,
        String email,
        String profileImage
) {
    public static UserProfileResponse of(User user) {
        return new UserProfileResponse(
                user.getUserName(),
                user.getEmail(),
                user.getProfileImage()
        );
    }
}
