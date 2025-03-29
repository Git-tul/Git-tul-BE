package io.gittul.app.infra.auth.oauth.provider.github.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubUserResponse(
    val id: String,

    @JsonProperty("login")
    val name: String,

    val email: String?,

    @JsonProperty("avatar_url")
    val profileImageUrl: String?,
)
