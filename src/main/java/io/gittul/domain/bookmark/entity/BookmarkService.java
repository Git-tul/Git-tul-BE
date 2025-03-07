package io.gittul.domain.bookmark.entity;

import io.gittul.domain.post.PostRepository;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.user.UserRepository;
import io.gittul.domain.user.entity.User;
import io.gittul.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addBookmark(User user, Long postId) {
        Post post = postRepository.getReferenceById(postId);
        Bookmark bookmark = Bookmark.of(user, post);

        if (post.isBookmarkedBy(user)) throw new CustomException(HttpStatus.CONFLICT, "이미 북마크한 게시글입니다.");

        user.getDetails().getBookmarks().add(bookmark);
        userRepository.save(user);
    }

    public void removeBookmark(User user, Long postId) {
        user.getDetails()
                .getBookmarks()
                .removeIf(bookmark -> bookmark.getPost().getPostId().equals(postId));

        userRepository.save(user);
    }
}
