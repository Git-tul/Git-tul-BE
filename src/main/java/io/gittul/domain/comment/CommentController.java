package io.gittul.domain.comment;

import io.gittul.domain.comment.dto.CommentCreateRequest;
import io.gittul.domain.comment.dto.CommentResponse;
import io.gittul.domain.like.LikeService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.aop.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/posts/{postId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping()
    public CommentResponse createComment(@Authenticated User user,
                                         @PathVariable Long postId,
                                         @Valid @RequestBody CommentCreateRequest request) {
        return commentService.createComment(request, postId, user);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@Authenticated User user,
                              @PathVariable Long postId,
                              @PathVariable Long id) {
        commentService.deleteComment(user, postId, id);
    }

    @PostMapping("/{id}/like")
    public void likeComment(@Authenticated User user,
                            @PathVariable Long id) {
        likeService.likeComment(user, id);
    }

    @DeleteMapping("/{id}/like")
    public void unLikeComment(@Authenticated User user,
                              @PathVariable Long id) {
        likeService.unLikeComment(user, id);
    }
}
