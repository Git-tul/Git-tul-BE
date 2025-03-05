package io.gittul.infra.ai.summery.dto;

import io.gittul.domain.github.entity.GitHubRepository;

public record SummaryAndRepository(
        RepositorySummary summary,
        GitHubRepository repository
) {
}
