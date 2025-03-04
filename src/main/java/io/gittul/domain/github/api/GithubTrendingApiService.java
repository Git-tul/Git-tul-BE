package io.gittul.domain.github.api;

import io.gittul.domain.github.GitHubRepositoryRepository;
import io.gittul.domain.github.api.dto.TrendingRepositoryApiResponse;
import io.gittul.domain.github.entity.GitHubRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GithubTrendingApiService {

    private final WebClient webClient;
    private final GitHubRepositoryRepository gitHubRepository;


    public GithubTrendingApiService(WebClient.Builder webClientBuilder,
                                    GitHubRepositoryRepository gitHubRepository) {
        String TRENDING_URL = "https://gtrend.yapie.me/repositories";
        this.webClient = webClientBuilder.baseUrl(TRENDING_URL).build();
        this.gitHubRepository = gitHubRepository;
    }

    private List<GitHubRepository> getTrendingRepositories(String period) {
        List<TrendingRepositoryApiResponse> trendingRepositories = webClient.get()
                .uri(period)
                .retrieve()
                .bodyToFlux(TrendingRepositoryApiResponse.class)
                .collectList()
                .block();

        return trendingRepositories.stream()
                .map(this::getOrCreateRepository)
                .toList();
    }

    public List<GitHubRepository> getDailyTrendingRepositories() {
        String DAILY_TRENDING_QUERY = "?since=daily";
        return getTrendingRepositories(DAILY_TRENDING_QUERY);
    }

    public List<GitHubRepository> getWeeklyTrendingRepositories() {
        String WEEKLY_TRENDING_QUERY = "?since=weekly";
        return getTrendingRepositories(WEEKLY_TRENDING_QUERY);
    }

    public List<GitHubRepository> getMonthlyTrendingRepositories() {
        String MONTHLY_TRENDING_QUERY = "?since=monthly";
        return getTrendingRepositories(MONTHLY_TRENDING_QUERY);
    }

    private GitHubRepository getOrCreateRepository(TrendingRepositoryApiResponse response) {
        return gitHubRepository.findByRepoUrl(response.url())
                .orElseGet(() -> gitHubRepository.save(
                        GitHubRepository.of(response.url(),
                                response.name(),
                                response.description(),
                                response.stars(),
                                response.forks()
                        )));
    }
}
