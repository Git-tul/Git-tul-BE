package io.gittul.app.domain.post

import io.gittul.app.domain.bookmark.BookmarkService
import io.gittul.app.domain.like.LikeService
import io.gittul.app.domain.post.dto.PostFeedResponse
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
@RequestMapping("/posts")
class PostSocialController(
    private val postService: PostService,
    private val likeService: LikeService,
    private val bookmarkService: BookmarkService
) {


    @GetMapping("/following")
    fun getFollowingPosts(
        page: PageQuery
    ): List<PostFeedResponse> {
        val user = AuthContext.getUser()
        return postService.getFollowingPosts(user, page.toRequest())
    }

    @GetMapping("/bookmark")
    fun getBookmarkedPosts(
        page: PageQuery
    ): List<PostFeedResponse> {
        val user = AuthContext.getUser()
        return postService.getBookmarkedPosts(user, page.toRequest())
    }

    @PostMapping("/{id}/like")
    fun likePost(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        likeService.likePost(user, id)
    }

    @PostMapping("/{id}/unlike")
    fun unLikePost(@PathVariable id: Long) {
        val user = AuthContext.getUser()
        likeService.unLikePost(user, id)
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
