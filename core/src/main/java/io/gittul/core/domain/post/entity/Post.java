package io.gittul.core.domain.post.entity;

import io.gittul.core.domain.bookmark.entity.Bookmark;
import io.gittul.core.domain.comment.entity.Comment;
import io.gittul.core.domain.github.entity.GitHubRepository;
import io.gittul.core.domain.like.entity.UserLikePost;
import io.gittul.core.domain.tag.entity.PostTag;
import io.gittul.core.domain.tag.entity.Tag;
import io.gittul.core.domain.user.entity.User;
import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "REPO_ID")
    private GitHubRepository repository;

    private String title;
    private String content;
    private String imageUrl;

    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<UserLikePost> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostTag> postTags = new ArrayList<>();

    public void addTag(Tag tag) {
        PostTag postTag = new PostTag(this, tag);
        postTags.add(postTag);
    }

    public int getLikeCount() {
        return likes.size();
    }

    public int getCommentCount() {
        return comments.size();
    }

    public int getBookmarkCount() {
        return bookmarks.size();
    }

    public Comment getBestComment() {
        return comments.stream()
                .max(Comparator.comparingInt(Comment::getLikeCount))
                .orElse(null);
    }

    public boolean isLikedBy(User requestingUser) {
        return likes.stream()
                .anyMatch(like -> like.getUser().getUserId().equals(requestingUser.getUserId()));
    }

    public boolean isBookmarkedBy(User requestingUser) {
        return bookmarks.stream()
                .anyMatch(bookmark -> bookmark.getUser().getUserId().equals(requestingUser.getUserId()));
    }

    public static Post of(User user,
                          String title,
                          String content,
                          String imageUrl) {
        Post post = new Post();
        post.user = user;
        post.title = title;
        post.content = content;
        post.imageUrl = imageUrl;
        return post;
    }

    public static Post of(User user,
                          String title,
                          String content,
                          String imageUrl,
                          GitHubRepository repository) {
        Post post = of(user, title, content, imageUrl);
        post.repository = repository;
        return post;
    }


    public void addTag(List<Tag> tags) {
        tags.forEach(this::addTag);
    }

    public void removeTag(Tag tag) {
        postTags.removeIf(postTag -> postTag.getTag().equals(tag));
    }

    public List<Tag> getTags() {
        return postTags.stream()
                .map(PostTag::getTag)
                .toList();
    }
}
