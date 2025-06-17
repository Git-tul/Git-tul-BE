package io.gittul.app.domain.thread.repository

import io.gittul.core.domain.thread.entity.Thread
import org.springframework.data.domain.Pageable
import java.util.Optional

interface ThreadQueryRepository {
    fun findAllWithDetails(page: Pageable): List<Thread>

    fun findByIdWithDetails(threadId: Long): Optional<Thread>

    fun findAllBookmarkedByUserId(userId: Long, page: Pageable): List<Thread>

    fun findByAuthorIds(userIds: List<Long>, page: Pageable): List<Thread>
}
