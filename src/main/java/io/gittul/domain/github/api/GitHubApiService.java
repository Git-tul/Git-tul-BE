package io.gittul.domain.github.api;

import io.gittul.domain.github.api.dto.RepositoryBasicInfoResponse;
import io.gittul.domain.github.api.dto.RepositoryInfo;
import io.gittul.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GitHubApiService {

    private final WebClient webClient; // Todo. webflux 걷어내기 고려

    @Value("${github.token}")
    private String githubToken;

    public GitHubApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public Mono<RepositoryInfo> getRepositoryInfo(String owner, String repo) {
        Mono<RepositoryBasicInfoResponse> basicInfo = webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .header("Authorization", authorizationHeader())
                .retrieve()
                .bodyToMono(RepositoryBasicInfoResponse.class)
                .doOnError(e -> log.error("기본 정보 가져오기 실패: {}/{}", owner, repo, e));

        Mono<String> readme = webClient.get()
                .uri("/repos/{owner}/{repo}/readme", owner, repo)
                .header("Authorization", authorizationHeader())
                .header("Accept", "application/vnd.github.v3.raw")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    log.warn("README가 없음: {}/{}", owner, repo);
                    return Mono.just("README 파일이 존재하지 않습니다.");
                });

        return Mono.zip(basicInfo, readme)
                .map(tuple -> {
                    RepositoryBasicInfoResponse basic = tuple.getT1();
                    String readmeContent = tuple.getT2();
                    return new RepositoryInfo(
                            basic,
                            readmeContent
                    );
                })
                .onErrorResume(e -> {
                    log.error("레포지토리 정보 조합 실패: {}/{}", owner, repo, e);
                    throw new CustomException("레포지토리 정보를 가져오는데 실패했습니다.");
                });
    }

    private String authorizationHeader() {
        return "Bearer " + githubToken;
    }
}

