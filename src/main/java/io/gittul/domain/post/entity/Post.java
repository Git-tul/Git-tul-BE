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

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<UserLikePost> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "POST_TAG",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tags = new ArrayList<>();
}
