package io.gittul.domain.post;

import io.gittul.domain.bookmark.entity.Bookmark;
import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.post.dto.NormalPostCreateRequest;
import io.gittul.domain.post.dto.PostDetailResponse;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.tag.TagService;
import io.gittul.domain.user.entity.User;
import io.gittul.global.page.PageUtil;
import io.gittul.global.exception.CustomException;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    public List<PostFeedResponse> getAllPosts(User user, PageRequest page) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(page);
        return posts.stream()
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPost(User user, Long id) {
        return PostDetailResponse.of(getOrElseThrow(id), user);
    }

    @Transactional(readOnly = true)
    public List<PostFeedResponse> getBookmarkedPosts(User user, PageRequest page) {
        Page<Bookmark> bookmarks = PageUtil.toPage(user.getDetails().getBookmarks(), page);

        return bookmarks.stream()
                .map(Bookmark::getPost)
                .map(post -> PostFeedResponse.ofAndTo(post, user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostFeedResponse> getFollowingPosts(User user, PageRequest page) {
        List<Long> followingUserIds = user.getDetails().getFollowings().stream()
                .map(follow -> follow.getFollowee().getUserId())
                .toList();

        Page<Post> posts = postRepository.findAllByUserUserIdInOrderByCreatedAtDesc(followingUserIds, page);

        return posts.stream()
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

    public PostFeedResponse createPostFromSummary(GitHubRepository repository,
                                                  RepositorySummary summary,
                                                  User admin) {
        Post post = Post.of(
                admin,
                repository,
                summary
        );
        post.addTag(tagService.getOrCreate(summary.tags()));

        Post savedPost = postRepository.save(post);
        return PostFeedResponse.ofNew(savedPost);
    }

    @Transactional
    public void deletePost(User user, Long id) {
        Post post = getOrElseThrow(id);

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    private Post getOrElseThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "글을 찾을 수 없습니다."));
    }
}
