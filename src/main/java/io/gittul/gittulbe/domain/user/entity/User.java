package io.gittul.gittulbe.domain.user.entity;

import io.gittul.gittulbe.domain.post.comment.entity.Comment;
import io.gittul.gittulbe.domain.post.comment.entity.UserLikeComment;
import io.gittul.gittulbe.domain.post.entity.Post;
import io.gittul.gittulbe.domain.post.entity.Tag;
import io.gittul.gittulbe.domain.post.entity.UserLikePost;
import io.gittul.gittulbe.global.EntityTimeStamp;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends EntityTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String email;
    private String password;

    @Embedded
    private OauthInfo oauthInfo;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<UserLikePost> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserLikeComment> likedComments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "USER_INTEREST",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> interests = new ArrayList<>();
}
