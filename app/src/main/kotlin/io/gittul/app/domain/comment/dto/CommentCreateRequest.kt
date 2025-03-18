package io.gittul.app.domain.comment.dto

import io.gittul.core.global.validation.OptionalURL
import jakarta.validation.constraints.Size

data class CommentCreateRequest(
    @Size(min = 1, max = 200)
    val content: String,

    @OptionalURL
    val image: String?
)
