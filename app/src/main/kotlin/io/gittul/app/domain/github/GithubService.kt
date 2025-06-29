package io.gittul.app.domain.github

import io.gittul.app.domain.github.document.GenerateTrendingRepoResult
import io.gittul.app.domain.thread.ThreadService
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.global.atEndOfDay
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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
class GithubService( // Todo. 정리
    private val apiService: GitHubApiService,
    private val trendingApiService: GithubTrendingApiService,
    private val summaryService: SummaryService,
    private val threadService: ThreadService,
    private val repository: GitHubRepositoryRepository,
    private val mongoService: MongoService
) {

    @Transactional
    fun getDailyTrendingRepositoriesSummery(admin: User): List<ThreadFeedResponse> {
        val trendingRepositories = trendingApiService.dailyTrendingRepositories()
        val processingResult = processTrendingRepositories(trendingRepositories, admin)

        saveGeneratingResult(processingResult)
        return processingResult.successResults
    }

    private data class ProcessingResult(
        val successResults: List<ThreadFeedResponse>,
        val errorMessages: List<String>
    )

    private fun processTrendingRepositories(
        trendingRepositories: List<TrendingRepositoryApiResponse>,
        admin: User
    ): ProcessingResult {
        val successResults = mutableListOf<ThreadFeedResponse>()
        val errorMessages = mutableListOf<String>()

        val newRepositories = getNewRepositoryInfos(trendingRepositories)
        val summariesAndRepos = generateRepositorySummaries(newRepositories, errorMessages)
        successResults.addAll(createThreadsFromSummaries(summariesAndRepos, admin))

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

    private fun createThreadsFromSummaries(
        summariesAndRepos: List<SummaryAndRepository>,
        admin: User
    ): List<ThreadFeedResponse> {
        val results = mutableListOf<ThreadFeedResponse>()
        summariesAndRepos.forEach {
            results.add(threadService.createThreadFromSummary(it.repository, it.summary, admin))
        }
        return results
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
        startDate: LocalDate,
        endDate: LocalDate
    ):
            List<GenerateTrendingRepoResult> {

        val startDateTime = startDate.atStartOfDay() // 00:00:00
        val endDateTime = endDate.atEndOfDay() // 23:59:59

        return mongoService.findAllByDateRange(
            GenerateTrendingRepoResult::class.java,
            startDateTime,
            endDateTime
        )
    }

    private fun getNewRepositoryInfos(trendingRepositories: List<TrendingRepositoryApiResponse>)
            : List<RepositoryInfo> {
        return trendingRepositories.parallelStream()
            .filter { isNew(it.url) }
            .map { apiService.getRepositoryInfo(it.author, it.name) }
            .filter { Objects.nonNull(it) }
            .toList()
    }

    private fun isNew(url: String): Boolean {
        return repository.findByRepoUrl(url).isEmpty
    }
}
