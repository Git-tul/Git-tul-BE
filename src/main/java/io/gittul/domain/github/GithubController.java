package io.gittul.domain.github;

import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Admin;
import io.gittul.infra.auth.aop.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/github")
@RestController
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubApiService;

    @Admin
    @GetMapping("/trending/daily")
    public List<PostFeedResponse> getDailyTrendingRepositories(@Authenticated User admin) {
        return githubApiService.getDailyTrendingRepositoriesSummery(admin);
    }
}
