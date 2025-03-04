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

    public static RepositoryBasicInfoResponse of(TrendingRepositoryApiResponse trending) {
        return new RepositoryBasicInfoResponse(
                trending.author(),
                trending.name(),
                trending.avatar(),
                trending.description(),
                trending.url(),
                trending.language(),
                trending.stars(),
                trending.forks()
        );
    }
}
