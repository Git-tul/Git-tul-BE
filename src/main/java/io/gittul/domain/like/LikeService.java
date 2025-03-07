package io.gittul.domain.like;

import io.gittul.domain.comment.CommentRepository;
import io.gittul.domain.comment.entity.Comment;
import io.gittul.domain.like.entity.UserLikeComment;
import io.gittul.domain.like.entity.UserLikePost;
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
public class LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void likePost(User user, Long postId) {
        Post post = postRepository.getReferenceById(postId);
        UserLikePost like = UserLikePost.of(user, post);

        if (post.isLikedBy(user)) throw new CustomException(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다.");

        user.getDetails().getLikedPosts().add(like);
        userRepository.save(user);
    }

    public void unLikePost(User user, Long postId) {
        user.getDetails()
                .getLikedPosts()
                .removeIf(like -> like.getPost().getPostId().equals(postId));
        userRepository.save(user);
    }

    public void likeComment(User user, Long postId) {
        Comment comment = commentRepository.getReferenceById(postId);
        UserLikeComment like = UserLikeComment.of(user, comment);

        if (comment.isLikedBy(user)) throw new CustomException(HttpStatus.CONFLICT, "이미 좋아요한 댓글입니다.");

        user.getDetails().getLikedComments().add(like);
        userRepository.save(user);
    }

    public void unLikeComment(User user, Long postId) {
        user.getDetails()
                .getLikedComments()
                .removeIf(like -> like.getComment().getCommentId().equals(postId));
        userRepository.save(user);
    }
}
