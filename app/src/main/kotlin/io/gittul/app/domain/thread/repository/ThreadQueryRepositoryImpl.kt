package io.gittul.app.domain.thread.repository

import com.querydsl.jpa.impl.JPAQuery
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
import java.util.*

@Repository
class ThreadQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ThreadQueryRepository {

    private val post = QPost.post
    private val user = QUser.user
    private val repo = QGitHubRepository.gitHubRepository
    private val likes = QUserLikePost.userLikePost
    private val bookmarks = QBookmark.bookmark
    private val postTag = QPostTag.postTag
    private val tag = QTag.tag

    override fun findAllWithDetails(page: Pageable): List<Post> {
        // 1. 페이징 위한 ID 목록만 추출
        val postIds = queryFactory
            .select(post.postId)
            .from(post)
            .orderBy(post.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        // 2. ID 기반 fetch join 조회
        return findByIds(postIds);
    }

    override fun findAllBookmarkedByUserId(userId: Long, page: Pageable): List<Post> {
        val postIds = queryFactory
            .select(QBookmark.bookmark.post.postId)
            .from(QBookmark.bookmark)
            .where(QBookmark.bookmark.user.userId.eq(userId))
            .orderBy(QBookmark.bookmark.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        if (postIds.isEmpty()) return emptyList()

        val posts = basePostQuery()
            .where(post.postId.`in`(postIds))
            .fetch()

        val postMap = posts.associateBy { it.postId }
        return postIds.mapNotNull { postMap[it] }
    }


    override fun findByAuthorIds(userIds: List<Long>, page: Pageable): List<Post> {
        val postIds = queryFactory
            .select(post.postId)
            .from(post)
            .where(post.user.userId.`in`(userIds))
            .orderBy(post.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        return findByIds(postIds);
    }


    override fun findByIdWithDetails(postId: Long): Optional<Post> {
        val result = basePostQuery()
            .where(post.postId.eq(postId))
            .fetchOne()

        return Optional.ofNullable(result)
    }

    private fun findByIds(postIds: List<Long>): List<Post> {
        if (postIds.isEmpty()) return emptyList()

        return basePostQuery()
            .where(post.postId.`in`(postIds))
            .orderBy(post.createdAt.desc())
            .fetch()
    }

    private fun basePostQuery(): JPAQuery<Post> {
        return queryFactory
            .selectFrom(post)
            .leftJoin(post.user, user).fetchJoin()
            .leftJoin(post.repository, repo).fetchJoin()
            .leftJoin(post.likes, likes).fetchJoin()
            .leftJoin(post.bookmarks, bookmarks).fetchJoin()
            .leftJoin(post.postTags, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .distinct()
    }

}
