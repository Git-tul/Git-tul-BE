package io.gittul.app.domain.comment

import io.gittul.app.domain.comment.dto.CommentCreateRequest
import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.like.LikeService
import io.gittul.app.global.logger
import io.gittul.app.infra.auth.aop.Authenticated
import io.gittul.core.domain.user.entity.User
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RequestMapping("/posts/{postId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
    private val likeService: LikeService
) {
    private val log = logger()

    @PostMapping
    fun createComment(
        @Authenticated user: User,
        @PathVariable postId: Long,
        @RequestBody request: @Valid CommentCreateRequest
    ): CommentResponse? {
        val comment = commentService.createComment(request, postId, user)

        log.info("[댓글 작성] {} 게시글에 {} 가 {} 댓글 작성", postId, user.email, comment.commentId)
        return CommentResponse.ofAndTo(comment, user)
    }

    @DeleteMapping("/{id}")
    fun deleteComment(
        @Authenticated user: User,
        @PathVariable postId: Long,
        @PathVariable id: Long
    ) {
        commentService.deleteComment(user, postId, id)
    }

    @PostMapping("/{id}/like")
    fun likeComment(
        @Authenticated user: User,
        @PathVariable id: Long
    ) {
        likeService.likeComment(user, id)
    }

    @DeleteMapping("/{id}/like")
    fun unLikeComment(
        @Authenticated user: User,
        @PathVariable id: Long
    ) {
        likeService.unLikeComment(user, id)
    }
}
