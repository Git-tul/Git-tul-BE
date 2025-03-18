package io.gittul.app.domain.post.dto

import io.gittul.core.global.validation.OptionalURL
import jakarta.validation.constraints.Size

data class NormalPostCreateRequest(
    @Size(min = 1, max = 30)
    val title: String,

    @Size(min = 1, max = 200)
    val content: String,

    @OptionalURL
    val image: String?,

    val tags: List<String>
)
