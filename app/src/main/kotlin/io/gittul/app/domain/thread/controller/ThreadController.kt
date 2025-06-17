package io.gittul.app.domain.thread.controller

import io.gittul.app.domain.thread.ThreadService
import io.gittul.app.domain.thread.dto.NormalThreadCreateRequest
import io.gittul.app.domain.thread.dto.ThreadDetailResponse
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import io.gittul.core.global.page.PageQuery
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@AccessGuard
@RestController
@RequestMapping("/threads")
class ThreadController(
    private val threadService: ThreadService
) {

    @GetMapping
    fun getAllThreads(
        page: PageQuery
    ): List<ThreadFeedResponse> {
        val user = AuthContext.getUser()

        return threadService.getAllTreads(user, page.toRequest())
    }

    @GetMapping("/{id}")
    fun getThread(
        @PathVariable id: Long
    ): ThreadDetailResponse {
        val user = AuthContext.getUser()

        return threadService.getThread(user, id)
    }

    @PostMapping
    fun createThread(
        @Valid @RequestBody request: NormalThreadCreateRequest
    ): ThreadDetailResponse {
        val user = AuthContext.getUser()

        return threadService.createThread(request, user)
    }

    @DeleteMapping("/{id}")
    fun deleteThread(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        threadService.deleteThread(user, id)
    }
}
