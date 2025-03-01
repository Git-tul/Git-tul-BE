package io.gittul.domain.user.entity;

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
import org.hibernate.annotations.Where;

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
    private String password;

    @Embedded
    private OauthInfo oauthInfo;

    @Builder.Default
    @Embedded
    private UserDetails userDetails = new UserDetails();

    public static User ofNormal(String userName, String email, String password) {
        return User.builder()
                .userName(userName)
                .email(email)
                .password(password)
                .build();
    }

    public static User ofOauth(String userName, String email, OauthInfo oauthInfo) {
        return User.builder()
                .userName(userName)
                .email(email)
                .oauthInfo(oauthInfo)
                .build();
    }
}
