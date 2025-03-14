package io.gittul.domain.github;

import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.UserInquiryService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.notify.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubBatchService {
    private final GithubService githubService;
    private final UserInquiryService userInquiryService;

    private final SlackService slackService;

    @Value("${gittul.bot.id}") // Todo. 토큰을쓸까
    private Long adminId;

    @Scheduled(cron = "0 30 0 * * ?")
    public void scheduleDailyTrendingRepositoriesSummery() {
        User admin = userInquiryService.getUserById(adminId);
        List<PostFeedResponse> responses = githubService.getDailyTrendingRepositoriesSummery(admin);

        log.info("[요약 생성] {} 개의 트렌딩 레포지토리 요약이 생성되었습니다.", responses.size());
        slackService.sendMessage("[오늘의 트랜딩 레포지토리 요약]", responses.stream()
                .collect(Collectors.toMap(PostFeedResponse::title, PostFeedResponse::description)));
    }
}
