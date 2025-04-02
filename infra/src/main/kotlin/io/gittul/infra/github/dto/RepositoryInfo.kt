package io.gittul.infra.github.dto

data class RepositoryInfo(
    val basicInfo: RepositoryBasicInfoResponse,
    val readme: String?
) {
    val fullName: String
        get() = "${basicInfo.owner}/${basicInfo.name}"
}
