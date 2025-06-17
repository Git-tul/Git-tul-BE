package io.gittul.core.domain.bookmark.entity;

import io.gittul.core.domain.thread.entity.Thread;
import io.gittul.core.domain.user.entity.User;
import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "THREAD_ID")
    private Thread thread;

    public static Bookmark of(User user, Thread thread) {
        Bookmark bookmark = new Bookmark();
        bookmark.user = user;
        bookmark.thread = thread;
        return bookmark;
    }
}
