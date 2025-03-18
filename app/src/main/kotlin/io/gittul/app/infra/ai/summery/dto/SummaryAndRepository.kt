package io.gittul.app.infra.ai.summery.dto

import io.gittul.core.domain.github.entity.GitHubRepository

data class SummaryAndRepository(
    val summary: RepositorySummary,
    val repository: GitHubRepository
)
