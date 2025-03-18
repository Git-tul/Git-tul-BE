package io.gittul.app.domain.post

import io.gittul.app.domain.post.dto.NormalPostCreateRequest
import io.gittul.app.domain.post.dto.PostDetailResponse
import io.gittul.app.domain.post.dto.PostFeedResponse
import io.gittul.app.domain.tag.TagService
import io.gittul.app.infra.ai.summery.dto.RepositorySummary
import io.gittul.core.domain.bookmark.entity.Bookmark
import io.gittul.core.domain.follow.entity.UserFollow
import io.gittul.core.domain.github.entity.GitHubRepository
import io.gittul.core.domain.post.entity.Post
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import io.gittul.core.global.page.PageUtil
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class PostService(
    private val postRepository: PostRepository,
    private val tagService: TagService
) {

    @Transactional(readOnly = true)
    fun getAllPosts(user: User, page: PageRequest): List<PostFeedResponse> {
        val posts = postRepository.findAllByOrderByCreatedAtDesc(page)
        return posts.stream()
            .map<PostFeedResponse> { PostFeedResponse.ofAndTo(it, user) }
            .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    fun getPost(user: User, id: Long): PostDetailResponse {
        return PostDetailResponse.of(getOrElseThrow(id), user)
    }

    @Transactional(readOnly = true)
    fun getBookmarkedPosts(user: User, page: PageRequest): List<PostFeedResponse> {
        val bookmarks = PageUtil.toPage<Bookmark>(user.details.bookmarks, page)

        return bookmarks.stream()
            .map(Bookmark::getPost)
            .map<PostFeedResponse> { PostFeedResponse.ofAndTo(it, user) }
            .toList()
    }

    @Transactional(readOnly = true)
    fun getFollowingPosts(user: User, page: PageRequest): List<PostFeedResponse> {
        val followingUserIds = user.details.followings.stream()
            .map<Long> { follow: UserFollow -> follow.followee.userId }
            .toList()

        val posts = postRepository.findAllByUserUserIdInOrderByCreatedAtDesc(followingUserIds, page)

        return posts.stream()
            .map<PostFeedResponse> { PostFeedResponse.ofAndTo(it, user) }
            .toList()
    }

    @Transactional
    fun createPost(request: NormalPostCreateRequest, currentUser: User): PostDetailResponse {
        val post = Post.of(
            currentUser,
            request.title,
            request.content,
            request.image
        )
        post.addTag(tagService.getOrCreate(request.tags))

        val savedPost = postRepository.save(post)
        return PostDetailResponse.of(savedPost, currentUser)
    }

    fun createPostFromSummary(
        repository: GitHubRepository,
        summary: RepositorySummary,
        admin: User
    ): PostFeedResponse {
        val post = Post.of(
            admin,
            summary.title,
            summary.description,
            null, // Todo. 리드미 등 이미지
            repository,
        )
        post.addTag(tagService.getOrCreate(summary.tags))

        val savedPost = postRepository.save(post)
        return PostFeedResponse.ofNew(savedPost)
    }

    @Transactional
    fun deletePost(user: User, id: Long) {
        val post = getOrElseThrow(id)

        if (post.user.userId != user.userId) {
            throw CustomException(HttpStatus.FORBIDDEN, "글을 삭제할 권한이 없습니다.")
        }

        postRepository.delete(post)
    }

    private fun getOrElseThrow(id: Long): Post {
        return postRepository.findById(id)
            .orElseThrow<CustomException>(Supplier { CustomException(HttpStatus.NOT_FOUND, "글을 찾을 수 없습니다.") })
    }
}
