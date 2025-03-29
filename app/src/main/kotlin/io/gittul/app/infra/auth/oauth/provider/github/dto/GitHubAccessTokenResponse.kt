package io.gittul.app.infra.auth.oauth.provider.github.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubAccessTokenResponse(

    @JsonProperty("access_token")
    val accessToken: String? = null,

    @JsonProperty("token_type")
    val tokenType: String? = null,

    val scope: String? = null,

    val error: String? = null,

    val errorDescription: String? = null,
) {
    fun isSuccess(): Boolean = accessToken != null && error == null
}
