package io.gittul.domain.github;

import io.gittul.domain.github.entity.GitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubRepositoryRepository extends JpaRepository<GitHubRepository, Long> {
}
