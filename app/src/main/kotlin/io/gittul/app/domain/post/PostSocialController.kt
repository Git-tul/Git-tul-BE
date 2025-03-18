package io.gittul.app.domain.post

import io.gittul.app.domain.bookmark.BookmarkService
import io.gittul.app.domain.like.LikeService
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.infra.auth.aop.Authenticated
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.page.PageQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostSocialController(
    private val postService: PostService,
    private val likeService: LikeService,
    private val bookmarkService: BookmarkService
) {


    @GetMapping("/following")
    fun getFollowingPosts(
        @Authenticated user: User,
        page: PageQuery
    ): List<PostFeedResponse> {
        return postService.getFollowingPosts(user, page.toRequest())
    }

    @GetMapping("/bookmark")
    fun getBookmarkedPosts(
        @Authenticated user: User,
        page: PageQuery
    ): List<PostFeedResponse> {
        return postService.getBookmarkedPosts(user, page.toRequest())
    }

    @PostMapping("/{id}/like")
    fun likePost(@Authenticated user: User, @PathVariable id: Long) {
        likeService.likePost(user, id)
    }

    @PostMapping("/{id}/unlike")
    fun unLikePost(@Authenticated user: User, @PathVariable id: Long) {
        likeService.unLikePost(user, id)
    }

    @PostMapping("/{id}/bookmark")
    fun addBookmark(@Authenticated user: User, @PathVariable id: Long) {
        bookmarkService.addBookmark(user, id)
    }

    @PostMapping("/{id}/unbookmark")
    fun removeBookmark(@Authenticated user: User, @PathVariable id: Long) {
        bookmarkService.removeBookmark(user, id)
    }
}
