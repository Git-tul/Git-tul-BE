package io.gittul.app.domain.comment.dto

import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.comment.entity.Comment
import io.gittul.core.domain.user.entity.User

data class CommentResponse(
    val user: UserProfileResponse?,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val likeCount: Int,
    val id: Long,
    val isLiked: Boolean,
    val replies: List<CommentResponse?>
) {
    companion object {
        fun ofAndTo(comment: Comment?, requestingUser: User?): CommentResponse? {
            if (comment == null) return null

            return CommentResponse(
                user = UserProfileResponse.of(comment.user),
                content = comment.content ?: "",
                createdAt = comment.createdAt?.toString() ?: "",
                updatedAt = comment.updatedAt?.toString() ?: "",
                likeCount = comment.likeCount,
                id = comment.commentId,
                isLiked = comment.isLikedBy(requestingUser),
                replies = emptyList()  // Todo. 대댓글 추가
            )
        }
    }
}
