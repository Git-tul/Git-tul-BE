package io.gittul.domain.github.api.dto;

public record TrendingRepositoryApiResponse(
        String author,
        String name,
        String avatar,
        String description,
        String url,
        String language,
        int stars,
        int forks,
        int currentPeriodStars
) {
}
