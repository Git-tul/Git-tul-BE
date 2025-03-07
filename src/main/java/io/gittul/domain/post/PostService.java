package io.gittul.domain.post;

import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.post.dto.NormalPostCreateRequest;
import io.gittul.domain.post.dto.PostDetailResponse;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.tag.TagService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;

    @Transactional(readOnly = true)
    public List<PostFeedResponse> getAllPosts(User user) {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDetailResponse createPost(NormalPostCreateRequest request, User currentUser) {
        Post post = Post.of(
                currentUser,
                request.title(),
                request.content(),
                request.image()
        );
        post.addTag(tagService.getOrCreate(request.tags()));

        Post savedPost = postRepository.save(post);
        return PostDetailResponse.of(savedPost, currentUser);
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

    public PostDetailResponse getPost(User user, Long id) {
        return postRepository.findById(id)
                .map(post -> PostDetailResponse.of(post, user))
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public List<PostFeedResponse> getFollowingPosts(User user) {
        List<Long> followingUserIds = user.getDetails().getFollowings().stream()
                .map(follow -> follow.getFollowee().getUserId())
                .toList();

        return postRepository.findAllByUserUserIdInOrderByCreatedAtDesc(followingUserIds).stream()
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .collect(Collectors.toList());
    }

    public void deletePost(User user, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("You can't delete other user's post");
        }

        postRepository.delete(post);
    }
}
