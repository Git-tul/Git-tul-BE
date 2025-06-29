package io.gittul.infra.github

import io.gittul.infra.github.dto.TrendingRepositoryApiResponse
import io.gittul.infra.global.logger
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@Service
class GithubTrendingApiService(
    private val githubTrendingRestClient: RestClient
) {
    companion object {
        private const val TRENDING_BASE_URL = "https://n8n.miensoap.me/webhook/trending"
    }

    fun dailyTrendingRepositories(): List<TrendingRepositoryApiResponse> {
        val url = TRENDING_BASE_URL
        return try {
            githubTrendingRestClient
                .get()
                .uri(url)
                .retrieve()
                .body(Array<TrendingRepositoryApiResponse>::class.java)?.toList()
                ?: emptyList()
        } catch (e: HttpClientErrorException) {
            logger().error("트렌딩 API 호출 실패: ${e.message}")
            emptyList()
        }
    }
}
