package io.gittul.domain.github;

import io.gittul.domain.user.UserInquiryService;
import io.gittul.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubBatchService {
    private final GithubService githubService;
    private final UserInquiryService userInquiryService;

    @Value("${gittul.bot.id}") // Todo. 토큰을쓸까
    private Long adminId;

    @Scheduled(cron = "0 30 0 * * ?")
    public void scheduleDailyTrendingRepositoriesSummery() {
        User admin = userInquiryService.getUserById(adminId);
        githubService.getDailyTrendingRepositoriesSummery(admin);
    }
}
