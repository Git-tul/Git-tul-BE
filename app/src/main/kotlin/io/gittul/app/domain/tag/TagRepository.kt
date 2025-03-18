package io.gittul.app.domain.tag

import io.gittul.core.domain.tag.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findAllByTagNameIn(tagNames: MutableList<String>): MutableSet<Tag>
}
