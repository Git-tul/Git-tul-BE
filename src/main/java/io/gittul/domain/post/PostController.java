package io.gittul.domain.post;

import io.gittul.domain.post.dto.NormalPostCreateRequest;
import io.gittul.domain.post.dto.PostDetailResponse;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Todo. 비로그인으로는 조회 못하나?
    @GetMapping()
    public List<PostFeedResponse> getAllPosts(@Authenticated User user) {
        return postService.getAllPosts(user);
    }

    @GetMapping("/{id}")
    public PostDetailResponse getPost(@Authenticated User user, @PathVariable Long id) {
        return postService.getPost(user, id);
    }

    @GetMapping("/following")
    public List<PostFeedResponse> getFollowingPosts(@Authenticated User user) {
        return postService.getFollowingPosts(user);
    }

    @PostMapping()
    public PostDetailResponse createPost(@Authenticated User user,
                                         @Valid @RequestBody NormalPostCreateRequest request) {
        return postService.createPost(request, user);
    }

    @PostMapping("/{id}/like")
    public void likePost(@Authenticated User user, @PathVariable Long id) {
        likeService.likePost(user, id);
    }

    @PostMapping("/{id}/unlike")
    public void unLikePost(@Authenticated User user, @PathVariable Long id) {
        likeService.unLikePost(user, id);
    }

    @PostMapping("/{id}/bookmark")
    public void addBookmark(@Authenticated User user, @PathVariable Long id) {
        bookmarkService.addBookmark(user, id);
    }

    @PostMapping("/{id}/unbookmark")
    public void removeBookmark(@Authenticated User user, @PathVariable Long id) {
        bookmarkService.removeBookmark(user, id);
    }
}
