package io.gittul.infra.github.dto

data class TrendingRepositoryApiResponse(
    val author: String,
    val name: String,
    val url: String,
    val currentPeriodStars: Int
)
