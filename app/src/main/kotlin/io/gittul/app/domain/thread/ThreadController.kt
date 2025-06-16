package io.gittul.app.domain.thread

import io.gittul.app.domain.thread.dto.NormalThreadCreateRequest
import io.gittul.app.domain.thread.dto.ThreadDetailResponse
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import io.gittul.core.global.page.PageQuery
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@AccessGuard
@RestController
@RequestMapping("/posts")
class ThreadController(
    private val treadService: TreadService
) {

    @GetMapping
    fun getAllPosts(
        page: PageQuery
    ): List<ThreadFeedResponse> {
        val user = AuthContext.getUser()

        return treadService.getAllPosts(user, page.toRequest())
    }

    @GetMapping("/{id}")
    fun getPost(
        @PathVariable id: Long
    ): ThreadDetailResponse {
        val user = AuthContext.getUser()

        return treadService.getPost(user, id)
    }

    @PostMapping
    fun createPost(
        @Valid @RequestBody request: NormalThreadCreateRequest
    ): ThreadDetailResponse {
        val user = AuthContext.getUser()

        return treadService.createPost(request, user)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        treadService.deletePost(user, id)
    }
}
