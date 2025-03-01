package io.gittul.gittulbe.domain.github.entity;

import io.gittul.gittulbe.global.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Developer extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    private String githubUserName;

    private Integer totalStars;

    private Integer totalForks;

    private LocalDateTime lastUpdatedAt;
}
