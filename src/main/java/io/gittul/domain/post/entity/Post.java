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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLikePost> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    public static Post ofNormal(String title,
                                String content) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        return post;
    }

    public static Post ofRepository(String title,
                                    String content,
                                    GitHubRepository repository) {
        Post post = ofNormal(title, content);
        post.repository = repository;
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
}
