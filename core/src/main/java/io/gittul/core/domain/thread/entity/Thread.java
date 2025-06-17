package io.gittul.core.domain.thread.entity;

import io.gittul.core.domain.bookmark.entity.Bookmark;
import io.gittul.core.domain.comment.entity.Comment;
import io.gittul.core.domain.github.entity.GitHubRepository;
import io.gittul.core.domain.like.entity.userLikeThread;
import io.gittul.core.domain.tag.entity.Tag;
import io.gittul.core.domain.tag.entity.ThreadTag;
import io.gittul.core.domain.user.entity.User;
import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Getter
@Table(
        name = "THREAD",
        indexes = {
                @Index(name = "idx_thread_user", columnList = "USER_ID"),
                @Index(name = "idx_thread_repo", columnList = "REPO_ID"),
                @Index(name = "idx_thread_created_at", columnList = "CREATED_AT")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thread extends EntityTimeStamp {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THREAD_ID", nullable = false)
    private Long threadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPO_ID")
    private GitHubRepository repository;

    private String title;
    private String content;
    private String imageUrl;

    private int viewCount;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "thread", fetch = FetchType.LAZY)
    private Set<userLikeThread> likes = new HashSet<>();

    @OneToMany(mappedBy = "thread", fetch = FetchType.LAZY)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ThreadTag> threadTags = new ArrayList<>();

    public void addTag(Tag tag) {
        ThreadTag threadTag = new ThreadTag(this, tag);
        threadTags.add(threadTag);
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

    public static Thread of(User user,
                            String title,
                            String content,
                            String imageUrl) {
        Thread thread = new Thread();
        thread.user = user;
        thread.title = title;
        thread.content = content;
        thread.imageUrl = imageUrl;
        return thread;
    }

    public static Thread of(User user,
                            String title,
                            String content,
                            String imageUrl,
                            GitHubRepository repository) {
        Thread thread = of(user, title, content, imageUrl);
        thread.repository = repository;
        return thread;
    }


    public void addTag(List<Tag> tags) {
        tags.forEach(this::addTag);
    }

    public void removeTag(Tag tag) {
        threadTags.removeIf(threadTag -> threadTag.getTag().equals(tag));
    }

    public List<Tag> getTags() {
        return threadTags.stream()
                .map(ThreadTag::getTag)
                .toList();
    }
}
