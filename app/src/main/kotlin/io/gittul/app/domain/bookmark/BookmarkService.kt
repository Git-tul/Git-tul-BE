package io.gittul.app.domain.bookmark

import io.gittul.app.domain.post.PostRepository
import io.gittul.app.domain.user.UserRepository
import io.gittul.core.domain.bookmark.entity.Bookmark
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class BookmarkService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    fun addBookmark(user: User, postId: Long) {
        val post = postRepository.getReferenceById(postId)
        val bookmark = Bookmark.of(user, post)

        if (post.isBookmarkedBy(user)) throw CustomException(HttpStatus.CONFLICT, "이미 북마크한 게시글입니다.")

        user.details.bookmarks.add(bookmark)
        userRepository.save(user)
    }

    fun removeBookmark(user: User, postId: Long) {
        user.details
            .bookmarks
            .removeIf { it.post.postId == postId }

        userRepository.save(user)
    }
}
