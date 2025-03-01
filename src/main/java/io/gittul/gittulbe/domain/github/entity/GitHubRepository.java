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
public class GitHubRepository extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repoId;

    private String repoUrl;

    private String name;

    private String description;

    private Integer starCount;

    private Integer forkCount;

    private LocalDateTime lastCrawledAt;
}
