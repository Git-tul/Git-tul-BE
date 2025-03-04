package io.gittul.domain.post.entity;

import io.gittul.domain.bookmark.entity.Bookmark;
import io.gittul.domain.comment.entity.Comment;
import io.gittul.domain.github.entity.GitHubRepository;
import io.gittul.domain.like.enriry.UserLikePost;
import io.gittul.domain.tag.entity.PostTag;
import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.user.entity.User;
import io.gittul.global.jpa.EntityTimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLikePost> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

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

    public void addTag(Tag tag) {
        PostTag postTag = new PostTag(this, tag);
        postTags.add(postTag);
        tag.getPostTags().add(postTag);
    }

    public void removeTag(Tag tag) {
        postTags.removeIf(postTag -> postTag.getTag().equals(tag));
        tag.getPostTags().removeIf(postTag -> postTag.getPost().equals(this));
    }

    public List<Tag> getTags() {
        return postTags.stream()
                .map(PostTag::getTag)
                .toList();
    }

    public int getLikeCount() {
        return likes.size();
    }

    public int getCommentCount() {
        return comments.size();
    }

    public Comment getBestComment() {
        return comments.stream()
                .max(Comparator.comparingInt(Comment::getLikeCount))
                .orElse(null);
    }

    public boolean isLikedBy(User user) {
        return likes.stream()
                .anyMatch(like -> like.getUser().getUserId().equals(user.getUserId()));
    }
}
