package io.gittul.app.domain.thread.controller

import io.gittul.app.domain.bookmark.BookmarkService
import io.gittul.app.domain.like.LikeService
import io.gittul.app.domain.thread.ThreadService
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import io.gittul.core.global.page.PageQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@AccessGuard
@RestController
@RequestMapping("/threads")
class TreadSocialController(
    private val threadService: ThreadService,
    private val likeService: LikeService,
    private val bookmarkService: BookmarkService
) {


    @GetMapping("/following")
    fun getFollowingThreads(
        page: PageQuery
    ): List<ThreadFeedResponse> {
        val user = AuthContext.getUser()
        return threadService.getFollowingThreads(user, page.toRequest())
    }

    @GetMapping("/bookmark")
    fun getBookmarkedThreads(
        page: PageQuery
    ): List<ThreadFeedResponse> {
        val user = AuthContext.getUser()
        return threadService.getBookmarkedThreads(user, page.toRequest())
    }

    @PostMapping("/{id}/like")
    fun likeThread(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        likeService.likeThread(user, id)
    }

    @PostMapping("/{id}/unlike")
    fun unLikeThread(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        likeService.unLikeThread(user, id)
    }

    @PostMapping("/{id}/bookmark")
    fun addBookmark(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        bookmarkService.addBookmark(user, id)
    }

    @PostMapping("/{id}/unbookmark")
    fun removeBookmark(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        bookmarkService.removeBookmark(user, id)
    }
}
