package io.gittul.app.infra.auth.oauth

import io.gittul.app.infra.auth.dto.TokenResponse
import io.gittul.app.infra.auth.oauth.provider.OauthProvider
import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import org.springframework.stereotype.Service

@Service
class OauthService(
    private val oauthProviders: Map<OauthProviderName, OauthProvider>
) {
    fun getAuthorizationUrl(provider: OauthProviderName, responseType: String): String {
        return oauthProviders[provider]?.getAuthorizationUrl(responseType)
            ?: throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다.")
    }

    fun login(provider: OauthProviderName, code: String, responseType: String): TokenResponse {
        return oauthProviders[provider]?.login(code, responseType)
            ?: throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다.")
    }
} 