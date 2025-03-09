package io.gittul.domain.post;

import io.gittul.domain.bookmark.entity.BookmarkService;
import io.gittul.domain.like.LikeService;
import io.gittul.domain.post.dto.PostFeedResponse;
import io.gittul.domain.user.entity.User;
import io.gittul.global.page.PageQuery;
import io.gittul.infra.auth.aop.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostSocialController {

    private final PostService postService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    @GetMapping("/following")
    public List<PostFeedResponse> getFollowingPosts(@Authenticated User user,
                                                    PageQuery page) {
        return postService.getFollowingPosts(user, page.toRequest());
    }

    @GetMapping("/bookmark")
    public List<PostFeedResponse> getBookmarkedPosts(@Authenticated User user,
                                                     PageQuery page) {
        return postService.getBookmarkedPosts(user, page.toRequest());
    }

    @PostMapping("/{id}/like")
    public void likePost(@Authenticated User user, @PathVariable Long id) {
        likeService.likePost(user, id);
    }

    @PostMapping("/{id}/unlike")
    public void unLikePost(@Authenticated User user, @PathVariable Long id) {
        likeService.unLikePost(user, id);
    }

    @PostMapping("/{id}/bookmark")
    public void addBookmark(@Authenticated User user, @PathVariable Long id) {
        bookmarkService.addBookmark(user, id);
    }

    @PostMapping("/{id}/unbookmark")
    public void removeBookmark(@Authenticated User user, @PathVariable Long id) {
        bookmarkService.removeBookmark(user, id);
    }
}
