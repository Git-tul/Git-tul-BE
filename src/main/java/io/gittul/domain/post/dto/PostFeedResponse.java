package io.gittul.domain.post.dto;

import io.gittul.domain.comment.dto.CommentResponse;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.user.dto.UserProfileResponse;
import io.gittul.domain.user.entity.User;

import java.util.List;

public record PostFeedResponse(
        UserProfileResponse user,
        String title,
        String image,
        String description,
        String createdAt,
        String updatedAt,
        long id,
        int startCount,
        int forkCount,
        int viewCount,
        int likeCount,
        int commentCount,
        CommentResponse bestComment,
        boolean isLiked,
        boolean isBookmarked,
        List<String> tags
) {

    // Todo. 명칭 수정 post -> thread
    public static PostFeedResponse ofAndTo(Post thread, User requestingUser) {
        // Todo. 일반 게시글인 경우 처리
        int startCount;
        int forkCount;

        if (thread.getRepository() == null) {
            startCount = 0;
            forkCount = 0;
        } else {
            startCount = thread.getRepository().getStarCount();
            forkCount = thread.getRepository().getForkCount();
        }

        return new PostFeedResponse(
                UserProfileResponse.of(thread.getUser()),
                thread.getTitle(),
                thread.getImageUrl(),
                thread.getContent(),
                thread.getCreatedAt().toString(),
                thread.getUpdatedAt().toString(),
                thread.getPostId(),
                startCount,
                forkCount,
                thread.getViewCount(),
                thread.getLikeCount(),
                thread.getCommentCount(),
                CommentResponse.ofAndTo(thread.getBestComment(), thread.getUser()),
                thread.isLikedBy(requestingUser),
                thread.isBookmarkedBy(requestingUser),
                thread.getTags().stream().map(Tag::getTagName).toList()
        );
    }

    public static PostFeedResponse ofNew(Post thread) {
        int startCount;
        int forkCount;

        if (thread.getRepository() == null) {
            startCount = 0;
            forkCount = 0;
        } else {
            startCount = thread.getRepository().getStarCount();
            forkCount = thread.getRepository().getForkCount();
        }

        return new PostFeedResponse(
                UserProfileResponse.of(thread.getUser()),
                thread.getTitle(),
                thread.getImageUrl(),
                thread.getContent(),
                thread.getCreatedAt().toString(),
                thread.getUpdatedAt().toString(),
                thread.getPostId(),
                startCount,
                forkCount,
                thread.getViewCount(),
                thread.getLikeCount(),
                thread.getCommentCount(),
                thread.getBestComment() != null ? CommentResponse.ofAndTo(thread.getBestComment(), thread.getUser()) : null,
                false,
                false,
                thread.getTags().stream().map(Tag::getTagName).toList()
        );
    }
}

