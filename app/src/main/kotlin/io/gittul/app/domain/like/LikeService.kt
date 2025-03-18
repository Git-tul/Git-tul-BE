package io.gittul.app.domain.like

import io.gittul.app.domain.comment.CommentRepository
import io.gittul.app.domain.post.PostRepository
import io.gittul.app.domain.user.UserRepository
import io.gittul.core.domain.like.entity.UserLikeComment
import io.gittul.core.domain.like.entity.UserLikePost
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {


    fun likePost(user: User, postId: Long) {
        val post: Post = postRepository.getReferenceById(postId)
        val like: UserLikePost = UserLikePost.of(user, post)

        if (post.isLikedBy(user)) throw CustomException(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다.")

        user.details.likedPosts.add(like)
        userRepository.save(user)
    }

    fun unLikePost(user: User, postId: Long) {
        user.details
            .likedPosts
            .removeIf { it.post.postId == postId }
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
