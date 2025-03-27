package io.gittul.app.domain.github

import io.gittul.app.domain.github.api.GitHubApiService
import io.gittul.app.domain.github.api.GithubTrendingApiService
import io.gittul.app.domain.github.api.dto.RepositoryInfo
import io.gittul.app.domain.github.api.dto.TrendingRepositoryApiResponse
import io.gittul.app.domain.post.PostService
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.global.logger
import io.gittul.app.infra.ai.summery.SummaryService
import io.gittul.app.infra.ai.summery.dto.SummaryAndRepository
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GithubService(
    private val apiService: GitHubApiService,
    private val trendingApiService: GithubTrendingApiService,
    private val summaryService: SummaryService,
    private val postService: PostService,
    private val repository: GitHubRepositoryRepository
) {

    fun saveRepository(info: RepositoryInfo): GitHubRepository {
        val basicInfo = info.basicInfo

        return repository.findByRepoUrl(basicInfo.url)
            .orElseGet {
                repository.save(
                    GitHubRepository.of(
                        basicInfo.url,
                        basicInfo.name,
                        basicInfo.description,
                        basicInfo.stars,
                        basicInfo.forks
                    )
                )
            }
    }

    @Transactional
    fun getDailyTrendingRepositoriesSummery(admin: User): List<PostFeedResponse> {
        val trendingRepositories = trendingApiService.dailyTrendingRepositories

        val newRepositories = getNewRepositoryInfos(trendingRepositories).parallelStream()
            .map {
                try {
                    val repository = saveRepository(it)
                    val summary = summaryService.generateSummary(it)
                    SummaryAndRepository(summary, repository)
                } catch (e: Exception) {
                    logger().error("Repository Summary 생성 실패: {}", e.message)
                    null
                }
            }
            .toList()
            .filterNotNull()

        return newRepositories.stream() // Note. 병렬 처리 시 태그 유니크 제약 조건 에러 가능성 주의
            .map {
                postService.createPostFromSummary(
                    it.repository,
                    it.summary,
                    admin
                )
            }
            .toList()
    }

    private fun getNewRepositoryInfos(trendingRepositories: List<TrendingRepositoryApiResponse>): List<RepositoryInfo> {
        return trendingRepositories.parallelStream()
            .map {
                apiService.getRepositoryInfo(
                    it.author,
                    it.name
                )
            }
            .filter { Objects.nonNull(it) }
            .filter { this.isNew(it) }
            .toList()
    }

    private fun isNew(info: RepositoryInfo): Boolean {
        return repository.findByRepoUrl(info.basicInfo.url).isEmpty
    }
}
