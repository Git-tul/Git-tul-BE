package io.gittul.app.domain.thread.dto

import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.thread.entity.Thread
import io.gittul.core.domain.user.entity.User
import java.math.BigInteger

data class ThreadDetailResponse(
    val id: BigInteger,
    val title: String,
    val content: String,
    val image: String?,
    val createdAt: String,
    val updatedAt: String,
    val writer: UserProfileResponse?,
    val isFollowed: Boolean,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likeCount: Int,
    val bookmarkCount: Int,
    val viewCount: Int,
    val tags: List<String>,
    val comments: List<CommentResponse>
) {
    companion object {
        fun of(thread: Thread, requestingUser: User): ThreadDetailResponse {
            return ThreadDetailResponse(
                id = BigInteger.valueOf(thread.threadId),
                title = thread.title,
                content = thread.content,
                image = thread.imageUrl,
                createdAt = thread.createdAt.toString(),
                updatedAt = thread.updatedAt.toString(),
                writer = UserProfileResponse.of(thread.user),
                isFollowed = requestingUser.isFollowing(thread.user),
                isLiked = thread.isLikedBy(requestingUser),
                isBookmarked = thread.isBookmarkedBy(requestingUser),
                likeCount = thread.likeCount,
                bookmarkCount = thread.bookmarkCount,
                viewCount = thread.viewCount,
                tags = thread.tags.map { it.tagName },
                comments = thread.comments.mapNotNull { CommentResponse.ofAndTo(it, thread.user) }
            )
        }
    }
}
