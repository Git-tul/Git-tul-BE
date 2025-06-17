package io.gittul.app.domain.comment

import io.gittul.app.domain.comment.dto.CommentCreateRequest
import io.gittul.app.domain.thread.repository.ThreadRepository
import io.gittul.core.domain.comment.entity.Comment
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val threadRepository: ThreadRepository,
    private val commentRepository: CommentRepository
) {

    @Transactional
    fun createComment(
        request: CommentCreateRequest,
        threadId: Long,
        user: User?
    ): Comment {
        val thread = threadRepository.findById(threadId)
            .orElseThrow {
                CustomException(
                    HttpStatus.NOT_FOUND,
                    "게시글을 찾을 수 없습니다."
                )
            }


        val comment = Comment.of(request.content, request.image, user, thread)
        thread.comments.add(comment)
        threadRepository.saveAndFlush(thread)

        return comment
    }

    fun deleteComment(user: User, id: Long) {
        val requestingComment = commentRepository.findById(id).orElseThrow {
            CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.")
        }

        val thread = requestingComment.thread

        if (requestingComment.user.userId != user.userId) {
            throw CustomException(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.")
        }

        thread.comments.remove(requestingComment)
        threadRepository.save(thread)
    }
}
