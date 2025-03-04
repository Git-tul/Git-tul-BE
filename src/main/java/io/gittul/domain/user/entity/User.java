package io.gittul.domain.user.entity;

import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.tag.entity.UserInterest;
import io.gittul.global.jpa.EntityTimeStamp;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
    private String password;

    @Embedded
    private OauthInfo oauthInfo;

    @Builder.Default
    @Embedded
    private UserDetails details = new UserDetails();

    public static User ofNormal(String userName,
                                String email,
                                String password,
                                String profileImage) {
        return User.builder()
                .userName(userName)
                .email(email)
                .profileImage(profileImage)
                .password(password)
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
}
