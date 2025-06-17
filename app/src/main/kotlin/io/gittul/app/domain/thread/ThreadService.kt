package io.gittul.app.domain.thread

import io.gittul.app.domain.tag.TagService
import io.gittul.app.domain.thread.dto.NormalThreadCreateRequest
import io.gittul.app.domain.thread.dto.ThreadDetailResponse
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.domain.thread.repository.ThreadQueryRepository
import io.gittul.app.domain.thread.repository.ThreadRepository
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.thread.entity.Thread
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import io.gittul.infra.summery.dto.RepositorySummary
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class ThreadService(
    private val threadRepository: ThreadRepository,
    private val threadQueryRepository: ThreadQueryRepository,
    private val tagService: TagService
) {

    @Transactional(readOnly = true)
    fun getAllTreads(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val threads = threadQueryRepository.findAllWithDetails(page)
        return threads.map { ThreadFeedResponse.ofAndTo(it, user) }
    }

    @Transactional(readOnly = true)
    fun getThread(user: User, id: Long): ThreadDetailResponse {
        return ThreadDetailResponse.of(getOrElseThrow(id), user)
    }

    @Transactional(readOnly = true)
    fun getBookmarkedThreads(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val threads = threadQueryRepository.findAllBookmarkedByUserId(user.userId, page)
        return threads.map { ThreadFeedResponse.ofAndTo(it, user) }
    }

    @Transactional(readOnly = true)
    fun getFollowingThreads(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val followingIds = user.details.followings.map { it.followee.userId }
        val threads = threadQueryRepository.findByAuthorIds(followingIds, page)
        return threads.map { ThreadFeedResponse.ofAndTo(it, user) }
    }

    @Transactional
    fun createThread(request: NormalThreadCreateRequest, currentUser: User): ThreadDetailResponse {
        val thread = Thread.of(
            currentUser,
            request.title,
            request.content,
            request.image
        )
        thread.addTag(tagService.getOrCreate(request.tags))

        val savedThread = threadRepository.save(thread)
        return ThreadDetailResponse.of(savedThread, currentUser)
    }

    fun createThreadFromSummary(
        repository: GitHubRepository,
        summary: RepositorySummary,
        admin: User
    ): ThreadFeedResponse {
        val thread = Thread.of(
            admin,
            summary.title,
            summary.description,
            null, // Todo. 리드미 등 이미지
            repository,
        )
        thread.addTag(tagService.getOrCreate(summary.tags))

        val savedThread = threadRepository.save(thread)
        return ThreadFeedResponse.ofNew(savedThread)
    }

    @Transactional
    fun deleteThread(user: User, id: Long) {
        val thread = getOrElseThrow(id)

        if (thread.user.userId != user.userId) {
            throw CustomException(HttpStatus.FORBIDDEN, "글을 삭제할 권한이 없습니다.")
        }

        threadRepository.delete(thread)
    }

    private fun getOrElseThrow(id: Long): Thread {
        return threadQueryRepository.findByIdWithDetails(id)
            .orElseThrow(Supplier { CustomException(HttpStatus.NOT_FOUND, "글을 찾을 수 없습니다.") })
    }
}
