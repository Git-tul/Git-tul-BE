package io.gittul.app.infra.ai.summery.dto

data class RepositorySummary(
    val title: String,
    val description: String,
    val tags: List<String>
)
