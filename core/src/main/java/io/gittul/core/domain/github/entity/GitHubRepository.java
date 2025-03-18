package io.gittul.core.domain.github.entity;

import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REPOSITORY")
public class GitHubRepository extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repoId;

    private String repoUrl;

    private String name;

    private String description;

    private Integer starCount;

    private Integer forkCount;

    public static GitHubRepository of(String repoUrl,
                                      String name,
                                      String description,
                                      Integer starCount,
                                      Integer forkCount) {
        GitHubRepository repository = new GitHubRepository();
        repository.repoUrl = repoUrl;
        repository.name = name;
        repository.description = description;
        repository.starCount = starCount;
        repository.forkCount = forkCount;
        return repository;
    }
}
