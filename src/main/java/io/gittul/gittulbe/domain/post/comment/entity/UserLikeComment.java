package io.gittul.gittulbe.domain.post.comment.entity;

import io.gittul.gittulbe.domain.user.entity.User;
import io.gittul.gittulbe.global.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class UserLikeComment extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment comment;
}
