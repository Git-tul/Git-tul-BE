package io.gittul.app.domain.github.api.dto

data class RepositoryInfo(
    val basicInfo: RepositoryBasicInfoResponse,
    val readme: String?
)
