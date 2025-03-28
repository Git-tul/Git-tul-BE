package io.gittul.infra.github.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class RepositoryBasicInfoResponse(
    val name: String,
    val owner: Owner,
    val description: String?,

    @JsonProperty("html_url")
    val url: String,

    val language: String?,

    @JsonProperty("stargazers_count")
    val stars: Int,

    @JsonProperty("forks_count")
    val forks: Int
) {
    override fun toString(): String { // OpenAI 프롬프트에 전달하기 위해 Json 으로 변환
        return jacksonObjectMapper().writeValueAsString(this);
    }
}

data class Owner(

    @JsonProperty("login")
    val author: String,

    @JsonProperty("avatar_url")
    val avatar: String
) {
    override fun toString(): String {
        return jacksonObjectMapper().writeValueAsString(this);
    }
}
