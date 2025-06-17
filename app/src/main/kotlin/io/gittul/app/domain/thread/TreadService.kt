package io.gittul.app.domain.thread

import io.gittul.app.domain.tag.TagService
import io.gittul.app.domain.thread.dto.NormalThreadCreateRequest
import io.gittul.app.domain.thread.dto.ThreadDetailResponse
import io.gittul.app.domain.thread.dto.ThreadFeedResponse
import io.gittul.app.domain.thread.repository.ThreadQueryRepository
import io.gittul.app.domain.thread.repository.ThreadRepository
import io.gittul.core.domain.bookmark.entity.Bookmark
import io.gittul.core.domain.follow.entity.UserFollow
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import io.gittul.core.global.page.PageUtil
import io.gittul.infra.summery.dto.RepositorySummary
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class TreadService(
    private val threadRepository: ThreadRepository,
    private val threadQueryRepository: ThreadQueryRepository,
    private val tagService: TagService
) {

    @Transactional(readOnly = true)
    fun getAllPosts(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val posts = threadQueryRepository.findAllWithDetails(page)
        return posts.map { ThreadFeedResponse.ofAndTo(it, user) }
    }

    @Transactional(readOnly = true)
    fun getPost(user: User, id: Long): ThreadDetailResponse {
        return ThreadDetailResponse.of(getOrElseThrow(id), user)
    }

    @Transactional(readOnly = true)
    fun getBookmarkedPosts(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val bookmarks = PageUtil.toPage<Bookmark>(user.details.bookmarks, page)

        return bookmarks.stream()
            .map(Bookmark::getPost)
            .map { ThreadFeedResponse.ofAndTo(it, user) }
            .toList()
    }

    @Transactional(readOnly = true)
    fun getFollowingPosts(user: User, page: PageRequest): List<ThreadFeedResponse> {
        val followingUserIds = user.details.followings.stream()
            .map { follow: UserFollow -> follow.followee.userId }
            .toList()

        val posts = threadRepository.findAllByUserUserIdInOrderByCreatedAtDesc(followingUserIds, page)

        return posts.stream()
            .map { ThreadFeedResponse.ofAndTo(it, user) }
            .toList()
    }

    @Transactional
    fun createPost(request: NormalThreadCreateRequest, currentUser: User): ThreadDetailResponse {
        val post = Post.of(
            currentUser,
            request.title,
            request.content,
            request.image
        )
        post.addTag(tagService.getOrCreate(request.tags))

        val savedPost = threadRepository.save(post)
        return ThreadDetailResponse.of(savedPost, currentUser)
    }

    fun createPostFromSummary(
        repository: GitHubRepository,
        summary: RepositorySummary,
        admin: User
    ): ThreadFeedResponse {
        val post = Post.of(
            admin,
            summary.title,
            summary.description,
            null, // Todo. 리드미 등 이미지
            repository,
        )
        post.addTag(tagService.getOrCreate(summary.tags))

        val savedPost = threadRepository.save(post)
        return ThreadFeedResponse.ofNew(savedPost)
    }

    @Transactional
    fun deletePost(user: User, id: Long) {
        val post = getOrElseThrow(id)

        if (post.user.userId != user.userId) {
            throw CustomException(HttpStatus.FORBIDDEN, "글을 삭제할 권한이 없습니다.")
        }

        threadRepository.delete(post)
    }

    private fun getOrElseThrow(id: Long): Post {
        return threadRepository.findById(id)
            .orElseThrow(Supplier { CustomException(HttpStatus.NOT_FOUND, "글을 찾을 수 없습니다.") })
    }
}
