package io.gittul.app.domain.post

import io.gittul.app.domain.post.dto.NormalPostCreateRequest
import io.gittul.app.domain.post.dto.PostDetailResponse
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import io.gittul.core.global.page.PageQuery
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@AccessGuard
@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun getAllPosts(
        page: PageQuery
    ): List<PostFeedResponse> {
        val user = AuthContext.getUser()

        return postService.getAllPosts(user, page.toRequest())
    }

    @GetMapping("/{id}")
    fun getPost(
        @PathVariable id: Long
    ): PostDetailResponse {
        val user = AuthContext.getUser()

        return postService.getPost(user, id)
    }

    @PostMapping
    fun createPost(
        @Valid @RequestBody request: NormalPostCreateRequest
    ): PostDetailResponse {
        val user = AuthContext.getUser()

        return postService.createPost(request, user)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        postService.deletePost(user, id)
    }
}
