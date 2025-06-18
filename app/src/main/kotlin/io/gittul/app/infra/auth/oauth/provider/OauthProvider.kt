package io.gittul.app.infra.auth.oauth.provider

import io.gittul.app.infra.auth.oauth.dto.OauthUserInfo
import io.gittul.app.infra.auth.dto.TokenResponse

abstract class OauthProvider(
    val providerName: OauthProviderName,
) {
    abstract fun getUserInfo(code: String, origin: String): OauthUserInfo

    protected fun getRedirectUrl(origin: String): String {
        val OAUTH_CALLBACK_PATH = "/auth/${this.providerName.name.lowercase()}/callback"

        return origin + OAUTH_CALLBACK_PATH;
    }

    fun getAuthorizationUrl(responseType: String): String
    fun login(code: String, responseType: String): TokenResponse
}
