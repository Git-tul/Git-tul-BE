package io.gittul.app.domain.like

import io.gittul.app.domain.comment.CommentRepository
import io.gittul.app.domain.thread.repository.ThreadRepository
import io.gittul.app.domain.user.UserRepository
import io.gittul.core.domain.like.entity.UserLikeComment
import io.gittul.core.domain.like.entity.userLikeThread
import io.gittul.core.domain.thread.entity.Thread
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val threadRepository: ThreadRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {


    fun likeThread(user: User, threadId: Long) {
        val thread: Thread = threadRepository.getReferenceById(threadId)
        val like: userLikeThread = userLikeThread.of(user, thread)

        if (thread.isLikedBy(user)) throw CustomException(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다.")

        user.details.likedThreads.add(like)
        userRepository.save(user)
    }

    fun unLikeThread(user: User, threadId: Long) {
        user.details
            .likedThreads
            .removeIf { it.thread.threadId == threadId }
        userRepository.save(user)
    }

    fun likeComment(user: User, commentId: Long) {
        val comment = commentRepository.getReferenceById(commentId)
        val like: UserLikeComment = UserLikeComment.of(user, comment)

        if (comment.isLikedBy(user)) throw CustomException(
            HttpStatus.CONFLICT,
            "이미 좋아요한 댓글입니다."
        )

        user.details.likedComments.add(like)
        userRepository.save(user)
    }

    fun unLikeComment(user: User, commentId: Long) {
        user.details
            .likedComments
            .removeIf { it.comment.commentId == commentId }
        userRepository.save(user)
    }
}
