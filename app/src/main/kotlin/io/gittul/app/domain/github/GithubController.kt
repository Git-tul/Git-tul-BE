package io.gittul.app.domain.github

import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.infra.auth.aop.Admin
import io.gittul.app.infra.auth.aop.Authenticated
import io.gittul.core.domain.user.entity.User
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/github")
@RestController
class GithubController(
    private val githubApiService: GithubService
) {

    @Admin
    @GetMapping("/trending/daily")
    fun getDailyTrendingRepositories(@Authenticated admin: User): List<PostFeedResponse?>? {
        return githubApiService.getDailyTrendingRepositoriesSummery(admin)
    }
}
