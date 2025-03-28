package io.gittul.infra.summery.dto

import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.infra.summery.dto.RepositorySummary

data class SummaryAndRepository(
    val summary: RepositorySummary,
    val repository: GitHubRepository
)
