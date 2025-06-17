package io.gittul.app.domain.thread.repository

import io.gittul.core.domain.thread.entity.Thread
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ThreadRepository : JpaRepository<Thread, Long> {
    fun findAllByUserUserIdInOrderByCreatedAtDesc(userIds: MutableList<Long>, pageable: Pageable): Page<Thread>

    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Thread>
}
