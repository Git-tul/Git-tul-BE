package io.gittul.app.domain.github.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RepositoryBasicInfoResponse(
    val author: String,
    val name: String,
    val avatar: String,
    val description: String,

    @JsonProperty("html_url")
    val url: String,
    val language: String,

    @JsonProperty("stargazers_count")
    val stars: Int,

    @JsonProperty("forks_count")
    val forks: Int
)
