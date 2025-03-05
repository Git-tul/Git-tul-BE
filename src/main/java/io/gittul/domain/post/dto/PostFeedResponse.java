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
        int starCount,
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
        return createPostFeedResponse(
                thread,
                thread.isLikedBy(requestingUser),
                thread.isBookmarkedBy(requestingUser)
        );
    }

    public static PostFeedResponse ofNew(Post thread) {
        return createPostFeedResponse(thread, false, false);
    }

    private static PostFeedResponse createPostFeedResponse(Post thread,
                                                           boolean isLiked,
                                                           boolean isBookmarked) {

        CommentResponse bestCommentResponse = thread.getBestComment() != null
                ? CommentResponse.ofAndTo(thread.getBestComment(), thread.getUser())
                : null;

        return new PostFeedResponse(
                UserProfileResponse.of(thread.getUser()),
                thread.getTitle(),
                thread.getImageUrl(),
                thread.getContent(),
                thread.getCreatedAt().toString(),
                thread.getUpdatedAt().toString(),
                thread.getPostId(),
                thread.getRepository() != null ? thread.getRepository().getStarCount() : 0,
                thread.getRepository() != null ? thread.getRepository().getForkCount() : 0, // Todo. 가독성 개선
                thread.getViewCount(),
                thread.getLikeCount(),
                thread.getCommentCount(),
                bestCommentResponse,
                isLiked,
                isBookmarked,
                thread.getTags().stream().map(Tag::getTagName).toList()
        );
    }
}

