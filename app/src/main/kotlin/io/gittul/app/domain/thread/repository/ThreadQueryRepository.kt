package io.gittul.app.domain.thread.repository

import io.gittul.core.domain.post.entity.Post
import org.springframework.data.domain.Pageable

interface ThreadQueryRepository {
    fun findAllWithDetails(page: Pageable): List<Post>
}
