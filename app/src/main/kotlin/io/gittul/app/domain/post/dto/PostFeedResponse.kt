package io.gittul.app.domain.post.dto

import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.user.entity.User

data class PostFeedResponse(
    val user: UserProfileResponse,
    val title: String,
    val image: String?,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val id: Long,
    val starCount: Int,
    val forkCount: Int,
    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val bestComment: CommentResponse?,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val tags: List<String>
) {
    companion object {
        // Todo. 명칭 수정 post -> thread
        fun ofAndTo(thread: Post, requestingUser: User?): PostFeedResponse {
            return createPostFeedResponse(
                thread,
                thread.isLikedBy(requestingUser),
                thread.isBookmarkedBy(requestingUser)
            )
        }

        fun ofNew(thread: Post): PostFeedResponse {
            return createPostFeedResponse(thread, false, false)
        }

        private fun createPostFeedResponse(
            thread: Post,
            isLiked: Boolean,
            isBookmarked: Boolean
        ): PostFeedResponse {
            val bestCommentResponse: CommentResponse? = thread.bestComment?.let {
                CommentResponse.ofAndTo(it, thread.user)
            }

            return PostFeedResponse(
                user = UserProfileResponse.of(thread.user),
                title = thread.title,
                image = thread.imageUrl,
                description = thread.content,
                createdAt = thread.createdAt.toString(),
                updatedAt = thread.updatedAt.toString(),
                id = thread.postId,
                starCount = thread.repository?.starCount ?: 0,
                forkCount = thread.repository?.forkCount ?: 0,  // Todo. 가독성 개선
                viewCount = thread.viewCount,
                likeCount = thread.likeCount,
                commentCount = thread.commentCount,
                bestComment = bestCommentResponse,
                isLiked = isLiked,
                isBookmarked = isBookmarked,
                tags = thread.tags.map { it.tagName }
            )
        }
    }
}
