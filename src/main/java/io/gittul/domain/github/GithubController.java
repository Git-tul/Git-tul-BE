package io.gittul.domain.github;

import io.gittul.domain.post.dto.PostFeedResponse;
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

    @GetMapping("/trending/daily")
    public List<PostFeedResponse> getDailyTrendingRepositories() {
        return githubApiService.getDailyTrendingRepositoriesSummery();
    }
}
