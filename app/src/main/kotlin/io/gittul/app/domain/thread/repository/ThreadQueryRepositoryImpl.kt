package io.gittul.app.domain.thread.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import io.gittul.core.domain.bookmark.entity.QBookmark
import io.gittul.core.domain.github.entity.QGitHubRepository
import io.gittul.core.domain.like.entity.QUserLikePost
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.post.entity.QPost
import io.gittul.core.domain.tag.entity.QPostTag
import io.gittul.core.domain.tag.entity.QTag
import io.gittul.core.domain.user.entity.QUser
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ThreadQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ThreadQueryRepository {

    override fun findAllWithDetails(page: Pageable): List<Post> {
        val post = QPost.post
        val user = QUser.user
        val repo = QGitHubRepository.gitHubRepository
        val likes = QUserLikePost.userLikePost
        val bookmarks = QBookmark.bookmark
        val postTag = QPostTag.postTag
        val tag = QTag.tag

        // 1. 페이징 위한 ID 목록만 추출
        val postIds = queryFactory
            .select(post.postId)
            .from(post)
            .orderBy(post.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        if (postIds.isEmpty()) return emptyList()

        // 2. ID 기반 fetch join 조회
        return queryFactory
            .selectFrom(post)
            .leftJoin(post.user, user).fetchJoin()
            .leftJoin(post.repository, repo).fetchJoin()
            .leftJoin(post.likes, likes).fetchJoin()
            .leftJoin(post.bookmarks, bookmarks).fetchJoin()
            .leftJoin(post.postTags, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .where(post.postId.`in`(postIds))
            .orderBy(post.createdAt.desc())
            .distinct()
            .fetch()
    }
}
