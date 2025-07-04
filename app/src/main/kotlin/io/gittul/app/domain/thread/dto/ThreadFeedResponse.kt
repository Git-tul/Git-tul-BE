package io.gittul.app.domain.thread.dto

import io.gittul.app.domain.comment.dto.CommentResponse
import io.gittul.app.domain.user.dto.UserProfileResponse
import io.gittul.core.domain.thread.entity.Thread
import io.gittul.core.domain.user.entity.User

data class ThreadFeedResponse(
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
        fun ofAndTo(thread: Thread, requestingUser: User?): ThreadFeedResponse {
            return createThreadFeedResponse(
                thread,
                thread.isLikedBy(requestingUser),
                thread.isBookmarkedBy(requestingUser)
            )
        }

        fun ofNew(thread: Thread): ThreadFeedResponse {
            return createThreadFeedResponse(thread, false, false)
        }

        private fun createThreadFeedResponse(
            thread: Thread,
            isLiked: Boolean,
            isBookmarked: Boolean
        ): ThreadFeedResponse {
            val bestCommentResponse: CommentResponse? = thread.bestComment?.let {
                CommentResponse.ofAndTo(it, thread.user)
            }

            return ThreadFeedResponse(
                user = UserProfileResponse.of(thread.user),
                title = thread.title,
                image = thread.imageUrl,
                description = thread.content,
                createdAt = thread.createdAt.toString(),
                updatedAt = thread.updatedAt.toString(),
                id = thread.threadId,
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
