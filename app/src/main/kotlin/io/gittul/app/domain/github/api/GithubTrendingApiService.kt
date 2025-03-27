package io.gittul.app.domain.github.api

import io.gittul.app.domain.github.api.dto.TrendingRepositoryApiResponse
import io.gittul.app.global.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@Service
class GithubTrendingApiService(
    private val githubTrendingRestClient: RestClient
) {
    private val log = logger()

    companion object {
        private const val TRENDING_BASE_URL = "https://gtrend.yapie.me"
        private const val REPOSITORIES_PATH = "/repositories"
    }

    fun getTrendingRepositories(period: String): List<TrendingRepositoryApiResponse> {
        val url = "$TRENDING_BASE_URL$REPOSITORIES_PATH?since=$period"
        return try {
            githubTrendingRestClient
                .get()
                .uri(url)
                .retrieve()
                .body(Array<TrendingRepositoryApiResponse>::class.java)?.toList()
                ?: emptyList()
        } catch (e: HttpClientErrorException) {
            log.error("트렌딩 API 호출 실패: ${e.message}")
            emptyList()
        }
    }

    val dailyTrendingRepositories: List<TrendingRepositoryApiResponse>
        get() = getTrendingRepositories("daily")
}
