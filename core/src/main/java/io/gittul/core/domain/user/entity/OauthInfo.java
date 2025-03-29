package io.gittul.core.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {
    private String oauthProvider;
    private String oauthId;

    public OauthInfo(
            String oauthProvider,
            String oauthId
    ) {
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
    }
}
