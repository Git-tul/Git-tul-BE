package io.gittul.app.domain.comment.dto

import io.gittul.core.global.validation.OptionalURL
import jakarta.validation.constraints.Size

data class CommentCreateRequest(
    @field:Size(min = 1, max = 200)
    val content: String,

    @field:OptionalURL
    val image: String?
)
