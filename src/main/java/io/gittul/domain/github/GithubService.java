package io.gittul.domain.github;

import io.gittul.domain.github.api.GitHubApiService;
import io.gittul.domain.github.api.GithubTrendingApiService;
import io.gittul.domain.github.api.dto.RepositoryBasicInfoResponse;
import io.gittul.domain.github.api.dto.RepositoryInfo;
import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.post.PostService;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.ai.summery.SummaryService;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import io.gittul.infra.ai.summery.dto.SummaryAndRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GitHubApiService apiService;
    private final GithubTrendingApiService trendingApiService;
    private final GitHubRepositoryRepository repository;

    private final SummaryService summaryService;
    private final PostService postService;


    public List<PostFeedResponse> getDailyTrendingRepositoriesSummery(User admin) {
        // Todo. 병렬처리
        List<SummaryAndRepository> repositories = trendingApiService.getDailyTrendingRepositories().stream()
                .map(repo -> apiService.getRepositoryInfo(repo.author(), repo.name()))
                .map(Mono::block).filter(Objects::nonNull)
                .map(info -> {
                    GitHubRepository repository = getOrCreateRepository(info);
                    RepositorySummary summary = summaryService.generateSummary(info);

                    return new SummaryAndRepository(summary, repository);
                })
                .toList();


        return repositories.stream()
                .map(repo -> postService.createPostFromSummary(repo.repository(), repo.summary(), admin))
                .toList();
    }

    private GitHubRepository getOrCreateRepository(RepositoryInfo info) {
        RepositoryBasicInfoResponse basicInfo = info.basicInfo();
        System.out.println(basicInfo.stars());

        return repository.findByRepoUrl(basicInfo.url())
                .orElseGet(() -> repository.save(
                        GitHubRepository.of(basicInfo.url(),
                                basicInfo.name(),
                                basicInfo.description(),
                                basicInfo.stars(),
                                basicInfo.forks()
                        )));
    }
}
