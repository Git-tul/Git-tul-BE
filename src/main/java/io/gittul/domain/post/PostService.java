package io.gittul.domain.post;

import io.gittul.domain.github.GitHubRepositoryRepository;
import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.post.dto.NormalPostCreateRequest;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.tag.TagService;
import io.gittul.domain.user.UserInquiryService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import io.gittul.infra.auth.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserInquiryService userInquiryService;
    private final PostRepository postRepository;
    private final TagService tagService;
    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    @Transactional(readOnly = true)
    public List<PostFeedResponse> getAllPosts(User user) {
        return postRepository.findAll().stream()
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostFeedResponse createPost(NormalPostCreateRequest request, TokenUserInfo currentUser) {
        User user = userInquiryService.getUserFromTokenInfo(currentUser);

        GitHubRepository repository = request.repoUrl() != null ?
                gitHubRepositoryRepository.findByRepoUrl(request.repoUrl())
                        .orElseThrow(() -> new IllegalArgumentException("Repository not found")) : null;


        Post post = Post.of(
                user,
                request.title(),
                request.content(),
                "" // Todo. 이미지 추가
        );
        post.addTag(tagService.getOrCreate(request.tags()));

        Post savedPost = postRepository.save(post);
        return PostFeedResponse.ofNew(savedPost);
    }

    public PostFeedResponse createPostFromSummary(GitHubRepository repository, RepositorySummary summary, User admin) {
        Post post = Post.of(
                admin,
                repository,
                summary
        );
        post.addTag(tagService.getOrCreate(summary.tags()));

        Post savedPost = postRepository.save(post);
        return PostFeedResponse.ofNew(savedPost);
    }

    public PostFeedResponse getPost(User user, Long id) {
        return postRepository.findById(id)
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
