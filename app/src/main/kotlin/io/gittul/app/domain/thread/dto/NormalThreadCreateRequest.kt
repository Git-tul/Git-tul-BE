package io.gittul.app.domain.thread.dto

import io.gittul.core.global.validation.OptionalURL
import jakarta.validation.constraints.Size

data class NormalThreadCreateRequest(
    @field:Size(min = 1, max = 30)
    val title: String,

    @field:Size(min = 1, max = 200)
    val content: String,

    @field:OptionalURL
    val image: String?,

    val tags: List<String>
)
