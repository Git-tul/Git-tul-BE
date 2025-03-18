package io.gittul.app.domain.github

import io.gittul.core.domain.github.entity.GitHubRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GitHubRepositoryRepository : JpaRepository<GitHubRepository, Long> {
    fun findByRepoUrl(repoUrl: String): Optional<GitHubRepository>
}
