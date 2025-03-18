package io.gittul.app.domain.github.api

import io.gittul.app.domain.github.api.dto.TrendingRepositoryApiResponse
import io.gittul.app.global.logger
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

@Service
class GithubTrendingApiService(httpClient: HttpClient) {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl(TRENDING_BASE_URL)
        .defaultHeader("Accept", "application/json")
        .clientConnector(ReactorClientHttpConnector(httpClient))
        .build()

    private fun getTrendingRepositories(period: String): Mono<List<TrendingRepositoryApiResponse>> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path(REPOSITORIES_PATH)
                    .queryParam("since", period)
                    .build()
            }
            .retrieve()
            .bodyToFlux(TrendingRepositoryApiResponse::class.java)
            .collectList()
            .doOnError { logger().error("API 호출 실패: ${it.message}") }
            .onErrorResume { Mono.just(emptyList()) }
    }

    val dailyTrendingRepositories: List<TrendingRepositoryApiResponse>
        get() = getTrendingRepositories("daily").block() ?: emptyList()

    companion object {
        private const val TRENDING_BASE_URL = "https://gtrend.yapie.me"
        private const val REPOSITORIES_PATH = "/repositories"
    }
}
