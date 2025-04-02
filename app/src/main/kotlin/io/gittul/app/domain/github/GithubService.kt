package io.gittul.app.domain.github

import io.gittul.app.domain.github.document.GenerateTrendingRepoResult
import io.gittul.app.domain.post.PostService
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.global.logger
import io.gittul.app.infra.MongoService
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.user.entity.User
import io.gittul.infra.github.GitHubApiService
import io.gittul.infra.github.GithubTrendingApiService
import io.gittul.infra.github.dto.RepositoryInfo
import io.gittul.infra.github.dto.TrendingRepositoryApiResponse
import io.gittul.infra.summery.SummaryService
import io.gittul.infra.summery.dto.SummaryAndRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GithubService( // Todo. 정리
    private val apiService: GitHubApiService,
    private val trendingApiService: GithubTrendingApiService,
    private val summaryService: SummaryService,
    private val postService: PostService,
    private val repository: GitHubRepositoryRepository,
    private val mongoService: MongoService
) {

    @Transactional
    fun getDailyTrendingRepositoriesSummery(admin: User): List<PostFeedResponse> {
        val trendingRepositories = trendingApiService.dailyTrendingRepositories
        val processingResult = processTrendingRepositories(trendingRepositories, admin)

        saveGeneratingResult(processingResult)
        return processingResult.successResults
    }

    private data class ProcessingResult(
        val successResults: List<PostFeedResponse>,
        val errorMessages: List<String>
    )

    private fun processTrendingRepositories(
        trendingRepositories: List<TrendingRepositoryApiResponse>,
        admin: User
    ): ProcessingResult {
        val successResults = mutableListOf<PostFeedResponse>()
        val errorMessages = mutableListOf<String>()

        val newRepositories = getNewRepositoryInfos(trendingRepositories)
        val summariesAndRepos = generateRepositorySummaries(newRepositories, errorMessages)
        successResults.addAll(createPostsFromSummaries(summariesAndRepos, admin))

        return ProcessingResult(successResults, errorMessages)
    }

    private fun generateRepositorySummaries(
        repositories: List<RepositoryInfo>,
        errorMessages: MutableList<String>
    ): List<SummaryAndRepository> {
        return repositories.parallelStream()
            .map { repoInfo ->
                try {
                    val repository = saveRepository(repoInfo)
                    val summary = summaryService.generateSummary(repoInfo)
                    SummaryAndRepository(summary, repository)
                } catch (e: Exception) {
                    val errorInfo = "${repoInfo.fullName}: ${e.message}"
                    logger().error("Repository Summary 생성 실패: {}", errorInfo)
                    errorMessages.add(errorInfo)
                    null
                }
            }
            .toList()
            .filterNotNull()
    }

    private fun createPostsFromSummaries(
        summariesAndRepos: List<SummaryAndRepository>,
        admin: User
    ): List<PostFeedResponse> {
        return summariesAndRepos.stream()
            .map { postService.createPostFromSummary(it.repository, it.summary, admin) }
            .toList()
    }

    private fun saveGeneratingResult(result: ProcessingResult) {
        mongoService.save(GenerateTrendingRepoResult.of(result.successResults, result.errorMessages))
    }

    fun saveRepository(info: RepositoryInfo): GitHubRepository {
        val basicInfo = info.basicInfo

        return repository.findByRepoUrl(basicInfo.url) // 이미 저장된 레포지토리 처리
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

    fun getDailyTrendingRepositoriesSummeryHistory(
        startDate: Date,
        endDate: Date
    ):
            List<GenerateTrendingRepoResult> {
        return mongoService.findAllByDateRange(
            GenerateTrendingRepoResult::class.java,
            startDate,
            endDate
        )
    }

    private fun getNewRepositoryInfos(trendingRepositories: List<TrendingRepositoryApiResponse>)
            : List<RepositoryInfo> {
        return trendingRepositories.parallelStream()
            .map { apiService.getRepositoryInfo(it.author, it.name) }
            .filter { Objects.nonNull(it) }
            .filter { isNew(it) }
            .toList()
    }

    private fun isNew(info: RepositoryInfo): Boolean {
        return repository.findByRepoUrl(info.basicInfo.url).isEmpty
    }
}
