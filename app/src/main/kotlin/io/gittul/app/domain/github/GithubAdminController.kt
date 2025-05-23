package io.gittul.app.domain.github

import io.gittul.app.domain.github.document.GenerateTrendingRepoResult
import io.gittul.app.infra.auth.aop.AuthContext
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.core.domain.user.entity.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@AccessGuard(role = Role.ADMIN)
@RequestMapping("/github")
@RestController
class GithubAdminController(
    private val githubApiService: GithubService
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * 호출 성공 결과를 반환
     * 실제 요약 게시글 생성은 비동기로 처리
     */

    @GetMapping("/trending/daily/generate")
    fun getDailyTrendingRepositories(): ResponseEntity<Void> {
        val admin = AuthContext.getUser()

        scope.launch {
            githubApiService.getDailyTrendingRepositoriesSummery(admin)
        }
        return ResponseEntity.ok().build()
    }

    /**
     * 요약 게시글 생성 결과를 조회
     * 기간을 명시하지 않을 시에 오늘 날짜 기준으로 조회
     */
    @GetMapping("/trending/daily/history")
    fun getDailyTrendingRepositoriesHistory(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") start: LocalDate = LocalDate.now(),
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate = LocalDate.now(),
    ): List<GenerateTrendingRepoResult> {
        return githubApiService.getDailyTrendingRepositoriesSummeryHistory(start, end)
    }
}
