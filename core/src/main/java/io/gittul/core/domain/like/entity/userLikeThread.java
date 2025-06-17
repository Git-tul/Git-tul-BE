package io.gittul.core.domain.like.entity;

import io.gittul.core.domain.thread.entity.Thread;
import io.gittul.core.domain.user.entity.User;
import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_LIKE_POST")
public class userLikeThread extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Thread thread;

    public static userLikeThread of(User user, Thread thread) {
        userLikeThread like = new userLikeThread();
        like.user = user;
        like.thread = thread;
        return like;
    }
}
