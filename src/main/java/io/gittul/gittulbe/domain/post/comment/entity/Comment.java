package io.gittul.gittulbe.domain.post.comment.entity;

import io.gittul.gittulbe.domain.post.entity.Post;
import io.gittul.gittulbe.domain.user.entity.User;
import io.gittul.gittulbe.global.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String content;

    @OneToMany(mappedBy = "comment")
    private List<UserLikeComment> likes = new ArrayList<>();
}
