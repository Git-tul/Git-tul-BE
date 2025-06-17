package io.gittul.app.domain.bookmark

import io.gittul.app.domain.thread.repository.ThreadRepository
import io.gittul.app.domain.user.UserRepository
import io.gittul.core.domain.bookmark.entity.Bookmark
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class BookmarkService(
    private val threadRepository: ThreadRepository,
    private val userRepository: UserRepository
) {

    fun addBookmark(user: User, threadId: Long) {
        val thread = threadRepository.getReferenceById(threadId)
        val bookmark = Bookmark.of(user, thread)

        if (thread.isBookmarkedBy(user)) throw CustomException(HttpStatus.CONFLICT, "이미 북마크한 게시글입니다.")

        user.details.bookmarks.add(bookmark)
        userRepository.save(user)
    }

    fun removeBookmark(user: User, threadId: Long) {
        user.details
            .bookmarks
            .removeIf { it.thread.threadId == threadId }

        userRepository.save(user)
    }
}
