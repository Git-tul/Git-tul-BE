package io.gittul.app.domain.github

import io.gittul.app.domain.user.UserInquiryService
import io.gittul.app.global.logger
import io.gittul.app.infra.notify.SlackService
import io.gittul.core.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class GitHubBatchService(
    private val githubService: GithubService,
    private val userInquiryService: UserInquiryService,
    private val slackService: SlackService
) {


    @Value("\${gittul.bot.id}") // Todo. 토큰을쓸까
    private val adminId: Long? = null

    @Scheduled(cron = "0 30 0 * * ?")
    fun scheduleDailyTrendingRepositoriesSummery() {
        val admin = userInquiryService.getUserById(adminId!!)
        val responses = githubService.getDailyTrendingRepositoriesSummery(admin)

        logger().info("[요약 생성] {} 개의 트렌딩 레포지토리 요약이 생성되었습니다.", responses.size)
        slackService.sendMessage(
            "[오늘의 트랜딩 레포지토리 요약]", responses.associate { it.title to it.description }
        )
    }
}
