package io.gittul.domain.github.api;

import io.gittul.domain.github.api.dto.TrendingRepositoryApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Slf4j
@Service
public class GithubTrendingApiService {

    private final WebClient webClient;
    private static final String TRENDING_BASE_URL = "https://gtrend.yapie.me";
    private static final String REPOSITORIES_PATH = "/repositories";

    // 생성자에서 WebClient 초기화
    public GithubTrendingApiService(HttpClient httpClient) {
        this.webClient = WebClient.builder()
                .baseUrl(TRENDING_BASE_URL)
                .defaultHeader("Accept", "application/json")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private Mono<List<TrendingRepositoryApiResponse>> getTrendingRepositories(String period) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(REPOSITORIES_PATH)
                        .queryParam("since", period)
                        .build())
                .retrieve()
                .bodyToFlux(TrendingRepositoryApiResponse.class)
                .collectList()
                .doOnError(e -> log.error("API 호출 실패: " + e.getMessage()))
                .onErrorResume(e -> Mono.just(List.of()));
    }

    public List<TrendingRepositoryApiResponse> getDailyTrendingRepositories() {
        return getTrendingRepositories("daily").block();
    }
}
