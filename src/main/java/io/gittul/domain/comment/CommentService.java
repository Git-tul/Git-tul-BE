package io.gittul.domain.comment;

import io.gittul.domain.comment.dto.CommentCreateRequest;
import io.gittul.domain.comment.dto.CommentResponse;
import io.gittul.domain.comment.entity.Comment;
import io.gittul.domain.post.PostRepository;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.user.entity.User;
import io.gittul.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;

    public CommentResponse createComment(CommentCreateRequest request,
                                         Long postId,
                                         User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));


        Comment comment = Comment.of(request.content(), request.image(), user, post);
        post.getComments().add(comment);
        postRepository.save(post);

        return CommentResponse.ofAndTo(comment, user);
    }

    public void deleteComment(User user, Long postId, Long id) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        Comment requestingComment = post.getComments().stream()
                .filter(comment -> comment.getCommentId().equals(id))
                .findAny()
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!requestingComment.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");
        }

        post.getComments().remove(requestingComment);
        postRepository.save(post);
    }
}
