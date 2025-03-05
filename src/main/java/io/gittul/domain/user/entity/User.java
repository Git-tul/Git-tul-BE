package io.gittul.domain.user.entity;

import io.gittul.domain.follow.entity.UserFollow;
import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.tag.entity.UserInterest;
import io.gittul.global.jpa.EntityTimeStamp;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends EntityTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String email;
    private String profileImage;

    @Embedded
    Password password;

    @Embedded
    private OauthInfo oauthInfo;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder.Default
    @Embedded
    private UserDetails details = new UserDetails();

    public static User ofNormal(String userName,
                                String email,
                                String profileImage,
                                String password) {
        return User.builder()
                .userName(userName)
                .email(email)
                .profileImage(profileImage)
                .password(new Password(password))
                .role(Role.USER)
                .build();
    }

    public static User ofOauth(String userName,
                               String email,
                               String profileImage,
                               OauthInfo oauthInfo) {
        return User.builder()
                .userName(userName)
                .email(email)
                .profileImage(profileImage)
                .oauthInfo(oauthInfo)
                .role(Role.USER)
                .build();
    }

    public void addInterest(Tag tag) {
        UserInterest userInterest = new UserInterest(this, tag);
        this.details.getInterests().add(userInterest);
        tag.getInterests().add(userInterest);
    }

    public void removeInterest(Tag tag) {
        this.details.getInterests().removeIf(userInterest -> userInterest.getTag().equals(tag));
        tag.getInterests().removeIf(userInterest -> userInterest.getUser().equals(this));
    }

    public void follow(User followee) {
        UserFollow userFollow = new UserFollow(this, followee);
        this.details.getFollowings().add(userFollow);
        followee.getDetails().getFollowers().add(userFollow);
    }

    public void unfollow(User followee) {
        this.details.getFollowings().removeIf(userFollow -> userFollow.getFollowee().equals(followee));
        followee.getDetails().getFollowers().removeIf(userFollow -> userFollow.getFollower().equals(this));
    }

    public boolean isFollowing(User user) {
        return this.details.getFollowings().stream()
                .anyMatch(follow -> follow.getFollowee().getUserId().equals(user.getUserId()));
    }
}
