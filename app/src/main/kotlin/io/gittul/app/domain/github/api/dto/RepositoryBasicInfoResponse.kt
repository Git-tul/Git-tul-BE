package io.gittul.app.domain.github.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RepositoryBasicInfoResponse(
    val author: String?,
    @JvmField val name: String?,
    val avatar: String?,
    @JvmField val description: String?,

    @JvmField @field:JsonProperty("html_url") @param:JsonProperty(
        "html_url"
    ) val url: String?,
    val language: String?,

    @JvmField @field:JsonProperty("stargazers_count") @param:JsonProperty(
        "stargazers_count"
    ) val stars: Int,
    @JvmField @field:JsonProperty("forks_count") @param:JsonProperty(
        "forks_count"
    ) val forks: Int
)
