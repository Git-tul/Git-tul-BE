package io.gittul.domain.user.dto;

import io.gittul.domain.follow.entity.UserFollow;
import io.gittul.domain.user.entity.User;
import io.gittul.domain.user.entity.UserDetails;

import java.util.List;

public record UserDetailResponse(
        Long userId,
        String nickname,
        String email,
        String profileImageUrl,
        Integer followerCount,
        Integer followingCount,
        List<UserProfileResponse> followings,
        Integer likedCount,
        List<String> interests
) {

    public static UserDetailResponse of(User user) {
        UserDetails details = user.getDetails();

        int likeCount = details.getPosts().stream()
                .mapToInt(post -> post.getLikes().size())
                .sum();

        return new UserDetailResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileImage(),
                details.getFollowers().size(),
                details.getFollowings().size(),
                details.getFollowings().stream()
                        .map(UserFollow::getFollowee)
                        .map(UserProfileResponse::of)
                        .toList(),
                likeCount,
                details.getInterests().stream()
                        .map(userInterest -> userInterest.getTag().getTagName())
                        .toList()
        );
    }
}
