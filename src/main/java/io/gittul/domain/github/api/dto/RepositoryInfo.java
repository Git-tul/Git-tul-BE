package io.gittul.domain.github.api.dto;

public record RepositoryInfo(
        RepositoryBasicInfoResponse basicInfo,
        String readme
) {
}
