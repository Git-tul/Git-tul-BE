package io.gittul.app.domain.thread.repository

import io.gittul.core.domain.post.entity.Post
import org.springframework.data.domain.Pageable
import java.util.Optional

interface ThreadQueryRepository {
    fun findAllWithDetails(page: Pageable): List<Post>

    fun findByIdWithDetails(postId: Long): Optional<Post>

    fun findAllBookmarkedByUserId(userId: Long, page: Pageable): List<Post>

    fun findByAuthorIds(userIds: List<Long>, page: Pageable): List<Post>
}
