package io.gittul.domain.post;

import io.gittul.domain.post.dto.PostDetailResponse;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
