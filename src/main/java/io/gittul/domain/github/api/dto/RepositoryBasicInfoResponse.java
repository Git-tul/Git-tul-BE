package io.gittul.domain.github.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryBasicInfoResponse(
        String author,
        String name,
        String avatar,
        String description,

        @JsonProperty("html_url")
        String url,
        String language,

        @JsonProperty("stargazers_count")
        int stars,
        @JsonProperty("forks_count")
        int forks
) {
}
