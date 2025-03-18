package io.gittul.app.domain.github

import io.gittul.app.domain.github.api.GitHubApiService
import io.gittul.app.domain.github.api.GithubTrendingApiService
import io.gittul.app.domain.github.api.dto.RepositoryInfo
import io.gittul.app.domain.github.api.dto.TrendingRepositoryApiResponse
import io.gittul.app.domain.post.PostService
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.infra.ai.summery.SummaryService
import io.gittul.app.infra.ai.summery.dto.SummaryAndRepository
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.user.entity.User
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class GithubService(
    private val apiService: GitHubApiService,
    private val trendingApiService: GithubTrendingApiService,
    private val repository: GitHubRepositoryRepository,
    private val summaryService: SummaryService,
    private val postService: PostService
) {

    fun saveRepository(info: RepositoryInfo): GitHubRepository {
        val basicInfo = info.basicInfo

        return repository.findByRepoUrl(basicInfo.url)
            .orElseGet(Supplier {
                repository.save<GitHubRepository?>(
                    GitHubRepository.of(
                        basicInfo.url,
                        basicInfo.name,
                        basicInfo.description,
                        basicInfo.stars,
                        basicInfo.forks
                    )
                )
            })
    }

    @Transactional
    fun getDailyTrendingRepositoriesSummery(admin: User): MutableList<PostFeedResponse?> {
        val trendingRepositories: List<TrendingRepositoryApiResponse> =
            trendingApiService.dailyTrendingRepositories

        val newRepositories = getNewRepositoryInfos(trendingRepositories).parallelStream()
            .map<SummaryAndRepository?> { info: RepositoryInfo? ->
                val repository = saveRepository(info!!)
                val summary = summaryService.generateSummary(info)
                SummaryAndRepository(summary, repository)
            }
            .toList()

        return newRepositories.stream() // Note. 병렬처리시 tag getOrSave 에서 유니크 제약 조건 에러 발생 가능성
            .map<PostFeedResponse?> {
                postService.createPostFromSummary(
                    it.repository,
                    it.summary,
                    admin
                )
            }
            .toList()
    }


    // Todo. 시간 비교 필요
    private fun getNewRepositoryInfos(trendingRepositories: List<TrendingRepositoryApiResponse>): List<RepositoryInfo> {
        return trendingRepositories.parallelStream()
            .map<RepositoryInfo?> {
                apiService.getRepositoryInfo(
                    it.author,
                    it.name
                ).block()
            }
            .filter { Objects.nonNull(it) }
            .filter { this.isNew(it) }
            .toList()
    }


    private fun isNew(info: RepositoryInfo): Boolean {
        return repository.findByRepoUrl(info.basicInfo.url).isEmpty
    }
}
