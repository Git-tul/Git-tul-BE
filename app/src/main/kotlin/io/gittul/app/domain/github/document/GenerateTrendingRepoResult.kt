package io.gittul.app.domain.github.document

import io.gittul.app.domain.post.dto.PostFeedResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "generate_trending_repo_result")
data class GenerateTrendingRepoResult(
    val generatedCount: Int,
    val failedCount: Int,

    val errors: List<String> = emptyList(),
    val results: List<PostFeedResponse>,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun of(feeds: List<PostFeedResponse>, errors: List<String>): GenerateTrendingRepoResult {
            return GenerateTrendingRepoResult(
                generatedCount = feeds.size,
                failedCount = errors.size,
                errors = errors,
                results = feeds
            )
        }
    }
}
