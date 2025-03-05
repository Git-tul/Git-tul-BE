package io.gittul.domain.post.dto;

import io.gittul.domain.comment.dto.CommentResponse;
import io.gittul.domain.post.entity.Post;
import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.user.dto.UserProfileResponse;
import io.gittul.domain.user.entity.User;

import java.math.BigInteger;
import java.util.List;

public record PostDetailResponse(
        BigInteger id,
        String title,
        String content,
        String image,
        String createdAt,
        String updatedAt,
        UserProfileResponse writer,
        boolean isFollowed,
        boolean isLiked,
        boolean isBookmarked,
        int likeCount,
        int bookmarkCount,
        int viewCount,
        List<String> tags,
        List<CommentResponse> comments
) {
    public static PostDetailResponse of(Post post, User requestingUser) {
        return new PostDetailResponse(
                BigInteger.valueOf(post.getPostId()),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString(),
                UserProfileResponse.of(post.getUser()),
                requestingUser.getDetails().isFollowing(post.getUser()),
                post.isLikedBy(requestingUser),
                post.isBookmarkedBy(requestingUser),
                post.getLikeCount(),
                post.getBookmarkCount(),
                post.getViewCount(),
                post.getTags().stream().map(Tag::getTagName).toList(),
                post.getComments().stream().map(comment -> CommentResponse.ofAndTo(comment, post.getUser())).toList()
        );
    }
}
