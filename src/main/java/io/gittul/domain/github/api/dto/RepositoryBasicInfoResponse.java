package io.gittul.domain.github.api.dto;

public record RepositoryBasicInfoResponse(
        String author,
        String name,
        String avatar,
        String description,
        String url,
        String language,
        int stars,
        int forks
) {
}
