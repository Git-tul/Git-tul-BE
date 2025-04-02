package io.gittul.app.infra.auth.oauth.dto

import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import io.gittul.core.domain.user.entity.OauthInfo

data class OauthUserInfo(
    val provider: OauthProviderName,
    val oauthId: String,
    val name: String,
    val email: String?,
    val profileImageUrl: String?
) {
    fun toOauthInfo(): OauthInfo {
        return OauthInfo(
            this.provider.name,
            this.oauthId,
        )
    }
}
