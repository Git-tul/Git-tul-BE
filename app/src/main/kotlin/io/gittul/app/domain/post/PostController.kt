package io.gittul.app.domain.post

import io.gittul.app.domain.post.dto.NormalPostCreateRequest
import io.gittul.app.domain.post.dto.PostDetailResponse
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.infra.auth.aop.Authenticated
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.page.PageQuery
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun getAllPosts(
        @Authenticated user: User,
        page: PageQuery
    ): List<PostFeedResponse> {
        return postService.getAllPosts(user, page.toRequest())
    }

    @GetMapping("/{id}")
    fun getPost(
        @Authenticated user: User,
        @PathVariable id: Long
    ): PostDetailResponse {
        return postService.getPost(user, id)
    }

    @PostMapping
    fun createPost(
        @Authenticated user: User,
        @Valid @RequestBody request: NormalPostCreateRequest
    ): PostDetailResponse {
        return postService.createPost(request, user)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@Authenticated user: User, @PathVariable id: Long) {
        postService.deletePost(user, id)
    }
}
