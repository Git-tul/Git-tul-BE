package io.gittul.domain.github;

import io.gittul.domain.github.api.GitHubApiService;
import io.gittul.domain.github.api.GithubTrendingApiService;
import io.gittul.domain.github.api.dto.RepositoryBasicInfoResponse;
import io.gittul.domain.github.api.dto.RepositoryInfo;
import io.gittul.domain.github.api.dto.TrendingRepositoryApiResponse;
import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.post.PostService;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.ai.summery.SummaryService;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import io.gittul.infra.ai.summery.dto.SummaryAndRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GitHubApiService apiService;
    private final GithubTrendingApiService trendingApiService;
    private final GitHubRepositoryRepository repository;

    private final SummaryService summaryService;
    private final PostService postService;

    public GitHubRepository saveRepository(RepositoryInfo info) {
        RepositoryBasicInfoResponse basicInfo = info.basicInfo();

        return repository.findByRepoUrl(basicInfo.url())
                .orElseGet(() -> repository.save(
                        GitHubRepository.of(basicInfo.url(),
                                basicInfo.name(),
                                basicInfo.description(),
                                basicInfo.stars(),
                                basicInfo.forks()
                        )));
    }

    @Transactional
    public List<PostFeedResponse> getDailyTrendingRepositoriesSummery(User admin) {
        List<TrendingRepositoryApiResponse> trendingRepositories = trendingApiService.getDailyTrendingRepositories();

        List<SummaryAndRepository> newRepositories = getNewRepositoryInfos(trendingRepositories).parallelStream()
                .map(info -> {
                    GitHubRepository repository = saveRepository(info);
                    RepositorySummary summary = summaryService.generateSummary(info);
                    return new SummaryAndRepository(summary, repository);
                })
                .toList();

        return newRepositories.stream() // Note. 병렬처리시 tag getOrSave 에서 유니크 제약 조건 에러 발생 가능성
                .map(repo -> postService.createPostFromSummary(repo.repository(), repo.summary(), admin))
                .toList();
    }


    // Todo. 시간 비교 필요
    private List<RepositoryInfo> getNewRepositoryInfos(List<TrendingRepositoryApiResponse> trendingRepositories) {
        return trendingRepositories.parallelStream()
                .map(repo -> apiService.getRepositoryInfo(repo.author(), repo.name()).block())
                .filter(Objects::nonNull)
                .filter(this::isNew)
                .toList();
    }


    private boolean isNew(RepositoryInfo info) {
        return repository.findByRepoUrl(info.basicInfo().url()).isEmpty();
    }
}
