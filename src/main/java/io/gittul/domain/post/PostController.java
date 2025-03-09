package io.gittul.domain.post;

import io.gittul.domain.post.dto.NormalPostCreateRequest;
import io.gittul.domain.post.dto.PostDetailResponse;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.global.page.PageQuery;
import io.gittul.infra.auth.aop.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    @GetMapping()
    public List<PostFeedResponse> getAllPosts(@Authenticated User user,
                                              PageQuery page) {
        return postService.getAllPosts(user, page.toRequest());
    }

    @GetMapping("/{id}")
    public PostDetailResponse getPost(@Authenticated User user,
                                      @PathVariable Long id) {
        return postService.getPost(user, id);
    }

    @PostMapping()
    public PostDetailResponse createPost(@Authenticated User user,
                                         @Valid @RequestBody NormalPostCreateRequest request) {
        return postService.createPost(request, user);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@Authenticated User user, @PathVariable Long id) {
        postService.deletePost(user, id);
    }
}
