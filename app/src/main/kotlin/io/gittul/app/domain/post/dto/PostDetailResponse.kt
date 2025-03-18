package io.gittul.app.domain.post.dto

import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.user.entity.User
import java.math.BigInteger

data class PostDetailResponse(
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
        fun of(post: Post, requestingUser: User): PostDetailResponse {
            return PostDetailResponse(
                id = BigInteger.valueOf(post.postId),
                title = post.title,
                content = post.content,
                image = post.imageUrl,
                createdAt = post.createdAt.toString(),
                updatedAt = post.updatedAt.toString(),
                writer = UserProfileResponse.of(post.user),
                isFollowed = requestingUser.isFollowing(post.user),
                isLiked = post.isLikedBy(requestingUser),
                isBookmarked = post.isBookmarkedBy(requestingUser),
                likeCount = post.likeCount,
                bookmarkCount = post.bookmarkCount,
                viewCount = post.viewCount,
                tags = post.tags.map { it.tagName },
                comments = post.comments.mapNotNull { CommentResponse.ofAndTo(it, post.user) }
            )
        }
    }
}
