package io.gittul.domain.follow;

import io.gittul.domain.follow.entity.UserFollow;
import io.gittul.domain.user.UserRepository;
import io.gittul.domain.user.dto.UserProfileResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;

    @Transactional
    public void follow(User follower, Long followeeId) {
        User followee = getUserOrElseThrow(followeeId);

        if (follower.getUserId().equals(followeeId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다.");
        }
        System.out.println(follower.isFollowing(followee));
        if (follower.isFollowing(followee)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 팔로우중인 유저입니다.");
        }

        follower.follow(followee);
        userRepository.save(follower);
    }

    @Transactional
    public void unfollow(User follower, Long followeeId) {
        User followee = getUserOrElseThrow(followeeId);

        if (!follower.isFollowing(followee)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "팔로우중인 유저가 아닙니다.");
        }

        follower.unfollow(followee);
        userRepository.save(follower);
    }

    public List<UserProfileResponse> getFollowers(User user) {
        return user.getDetails().getFollowers().stream()
                .map(UserFollow::getFollower)
                .map(UserProfileResponse::of)
                .toList();
    }

    public List<UserProfileResponse> getFollowings(User user) {
        return user.getDetails().getFollowings().stream()
                .map(UserFollow::getFollowee)
                .map(UserProfileResponse::of)
                .toList();
    }

    private User getUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
