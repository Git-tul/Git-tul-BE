package io.gittul.domain.comment.dto;

import io.gittul.domain.comment.entity.Comment;
import io.gittul.domain.user.dto.UserProfileResponse;
import io.gittul.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

public record CommentResponse(
        UserProfileResponse user,

        String content,

        String createdAt,

        String updatedAt,

        int likeCount,

        Long id,

        boolean isLiked,

        List<CommentResponse> replies
) {
    public static CommentResponse ofAndTo(Comment comment,
                                          User requestingUser) {
        return new CommentResponse(
                UserProfileResponse.of(comment.getUser()),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                comment.getUpdatedAt().toString(),
                comment.getLikeCount(),
                comment.getCommentId(),
                comment.isLikedBy(requestingUser),
                new ArrayList<>() // Todo. 대댓글 구현
        );
    }
}
