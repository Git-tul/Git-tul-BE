package io.gittul.app.domain.github.api.dto

data class TrendingRepositoryApiResponse(
    val author: String,
    val name: String,
    val url: String,
    val currentPeriodStars: Int
)
