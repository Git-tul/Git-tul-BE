package io.gittul.app.domain.thread.repository

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import io.gittul.core.domain.bookmark.entity.QBookmark
import io.gittul.core.domain.github.entity.QGitHubRepository
import io.gittul.core.domain.like.entity.QuserLikeThread
import io.gittul.core.domain.tag.entity.QTag
import io.gittul.core.domain.tag.entity.QThreadTag
import io.gittul.core.domain.thread.entity.QThread
import io.gittul.core.domain.thread.entity.Thread
import io.gittul.core.domain.user.entity.QUser
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ThreadQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ThreadQueryRepository {

    private val thread = QThread.thread
    private val user = QUser.user
    private val repo = QGitHubRepository.gitHubRepository
    private val likes = QuserLikeThread.userLikeThread
    private val bookmarks = QBookmark.bookmark
    private val threadTag = QThreadTag.threadTag
    private val tag = QTag.tag

    override fun findAllWithDetails(page: Pageable): List<Thread> {
        // 1. 페이징 위한 ID 목록만 추출
        val threadIds = queryFactory
            .select(thread.threadId)
            .from(thread)
            .orderBy(thread.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        // 2. ID 기반 fetch join 조회
        return findByIds(threadIds)
    }

    override fun findAllBookmarkedByUserId(userId: Long, page: Pageable): List<Thread> {
        val threadIds = queryFactory
            .select(QBookmark.bookmark.thread.threadId)
            .from(QBookmark.bookmark)
            .where(QBookmark.bookmark.user.userId.eq(userId))
            .orderBy(QBookmark.bookmark.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        if (threadIds.isEmpty()) return emptyList()

        val threads = baseThreadQuery()
            .where(thread.threadId.`in`(threadIds))
            .fetch()

        val threadMap = threads.associateBy { it.threadId }
        return threadIds.mapNotNull { threadMap[it] }
    }


    override fun findByAuthorIds(userIds: List<Long>, page: Pageable): List<Thread> {
        val threadIds = queryFactory
            .select(thread.threadId)
            .from(thread)
            .where(thread.user.userId.`in`(userIds))
            .orderBy(thread.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

        return findByIds(threadIds)
    }


    override fun findByIdWithDetails(threadId: Long): Optional<Thread> {
        val result = baseThreadQuery()
            .where(thread.threadId.eq(threadId))
            .fetchOne()

        return Optional.ofNullable(result)
    }

    private fun findByIds(threadIds: List<Long>): List<Thread> {
        if (threadIds.isEmpty()) return emptyList()

        return baseThreadQuery()
            .where(thread.threadId.`in`(threadIds))
            .orderBy(thread.createdAt.desc())
            .fetch()
    }

    private fun baseThreadQuery(): JPAQuery<Thread> {
        return queryFactory
            .selectFrom(thread)
            .leftJoin(thread.user, user).fetchJoin()
            .leftJoin(thread.repository, repo).fetchJoin()
            .leftJoin(thread.likes, likes).fetchJoin()
            .leftJoin(thread.bookmarks, bookmarks).fetchJoin()
            .leftJoin(thread.threadTags, threadTag).fetchJoin()
            .leftJoin(threadTag.tag, tag).fetchJoin()
            .distinct()
    }

}
