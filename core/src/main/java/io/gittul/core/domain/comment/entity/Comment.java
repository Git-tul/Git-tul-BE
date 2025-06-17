package io.gittul.core.domain.comment.entity;

import io.gittul.core.domain.like.entity.UserLikeComment;
import io.gittul.core.domain.thread.entity.Thread;
import io.gittul.core.domain.user.entity.User;
import io.gittul.core.global.jpa.EntityTimeStamp;
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
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "THREAD_ID")
    private Thread thread;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String content;

    private String imageUrl;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<UserLikeComment> likes = new ArrayList<>();

    public static Comment of(String content,
                             String imageUrl,
                             User user,
                             Thread thread) {
        Comment comment = new Comment();
        comment.content = content;
        comment.imageUrl = imageUrl;
        comment.user = user;
        comment.thread = thread;
        return comment;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public boolean isLikedBy(User requestingUser) {
        return likes.stream()
                .anyMatch(like -> like.getUser().getUserId().equals(requestingUser.getUserId()));
    }
}
