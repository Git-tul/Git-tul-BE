package io.gittul.domain.tag.entity;

import io.gittul.domain.post.entity.Post;
import io.gittul.domain.user.entity.User;
import io.gittul.global.jpa.EntityTimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterest> interests = new ArrayList<>();


    // Todo. public ?
    protected void addToPost(Post post) {
        PostTag postTag = new PostTag(post, this);
        postTags.add(postTag);
        post.getPostTags().add(postTag);
    }

    protected void removeFromPost(Post post) {
        postTags.removeIf(postTag -> postTag.getPost().equals(post));
        post.getPostTags().removeIf(postTag -> postTag.getTag().equals(this));
    }

    protected void addToInterest(User user) {
        UserInterest interest = new UserInterest(user, this);
        interests.add(interest);
        user.getDetails().getInterests().add(interest);
    }

    protected void removeFromInterest(User user) {
        interests.removeIf(interest -> interest.getUser().equals(user));
        user.getDetails().getInterests().removeIf(interest -> interest.getTag().equals(this));
    }
}
