package io.gittul.app.domain.post

import io.gittul.core.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByUserUserIdInOrderByCreatedAtDesc(userIds: MutableList<Long>, pageable: Pageable): Page<Post>

    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>
}
