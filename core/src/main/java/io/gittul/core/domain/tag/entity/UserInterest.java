package io.gittul.core.domain.tag.entity;

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

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterest extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    public UserInterest(User user, Tag tag) {
        this.user = user;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInterest interest = (UserInterest) o;
        return Objects.equals(user.getUserId(), interest.user.getUserId())
                && Objects.equals(tag.getTagName(), interest.tag.getTagName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId(), tag.getTagName());
    }
}
