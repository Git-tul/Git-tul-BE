package io.gittul.app.infra.auth.oauth.provider.github

import io.gittul.app.infra.auth.oauth.dto.OauthUserInfo
import io.gittul.app.infra.auth.oauth.provider.OauthProvider
import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import io.gittul.app.infra.auth.oauth.provider.github.dto.GitHubAccessTokenResponse
import io.gittul.app.infra.auth.oauth.provider.github.dto.GitHubUserResponse
import io.gittul.core.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.util.UriComponentsBuilder

@Component
class GitHubOauthProvider : OauthProvider(
    OauthProviderName.GITHUB
) {

    @Value("\${oauth.github.client}")
    private val clientId: String? = null

    @Value("\${oauth.github.secret}")
    private var clientSecret: String? = null

    private val scope: String = "user:email"
    private val restClient: RestClient = RestClient.create()

    override fun getUserInfo(code: String, origin: String): OauthUserInfo {
        val userInfoUrl = "https://api.github.com/user"
        val accessToken = getAccessToken(code, origin)

        val userInfoResponse = restClient.get()
            .uri(userInfoUrl)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body<GitHubUserResponse>()
            ?: throw CustomException("Failed to fetch user info from GitHub")

        println("GitHub User Info: $userInfoResponse")

        return OauthUserInfo(
            provider = this.providerName,
            oauthId = userInfoResponse.id,
            email = userInfoResponse.email,
            name = userInfoResponse.name,
            profileImageUrl = userInfoResponse.profileImageUrl,
        )
    }

    private fun getAccessToken(code: String, origin: String): String {
        val accessTokenUrl = UriComponentsBuilder.fromUriString("https://github.com/login/oauth/access_token")
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .queryParam("redirect_uri", this.getRedirectUrl(origin))
            .queryParam("scope", scope)
            .toUriString()

        val accessTokenResponse = restClient.post()
            .uri(accessTokenUrl)
            .header("accept", "application/json")
            .retrieve()
            .body<GitHubAccessTokenResponse>()

        if (accessTokenResponse?.accessToken == null) {
            throw CustomException("Failed to fetch access token from GitHub : " + accessTokenResponse?.error)
        }

        return accessTokenResponse.accessToken
    }
}


