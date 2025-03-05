package io.gittul.domain.github.api.dto;

public record TrendingRepositoryApiResponse(
        String author,
        String name,
        String url,
        int currentPeriodStars
) {
}
