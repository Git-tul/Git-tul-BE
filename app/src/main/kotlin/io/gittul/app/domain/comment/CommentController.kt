package io.gittul.app.domain.comment

import io.gittul.app.domain.comment.dto.CommentCreateRequest
import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.like.LikeService
import io.gittul.app.global.logger
import io.gittul.app.infra.auth.aop.AccessGuard
import io.gittul.app.infra.auth.aop.AuthContext
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@AccessGuard
@RequestMapping("/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
    private val likeService: LikeService
) {

    @PostMapping
    fun createComment(
        @RequestBody request: @Valid CommentCreateRequest
    ): CommentResponse? {
        val user = AuthContext.getUser();
        val comment = commentService.createComment(request, request.postId, user)

        logger().info("[댓글 작성] {} 게시글에 {} 가 {} 댓글 작성", request.postId, user.email, comment.commentId)
        return CommentResponse.ofAndTo(comment, user)
    }

    @DeleteMapping("/{id}")
    fun deleteComment(
        @PathVariable id: Long
    ) {
        val user = AuthContext.getUser();
        commentService.deleteComment(user, id)
    }

    @PostMapping("/{id}/like")
    fun likeComment(
        @PathVariable id: Long
    ) {
        val user = AuthContext.getUser();
        likeService.likeComment(user, id)
    }

    @DeleteMapping("/{id}/like")
    fun unLikeComment(
        @PathVariable id: Long
    ) {
        val user = AuthContext.getUser();
        likeService.unLikeComment(user, id)
    }
}
