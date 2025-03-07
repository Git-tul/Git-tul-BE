package io.gittul.domain.comment.dto;

import io.gittul.global.validation.OptionalURL;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @Size(min = 1, max = 200)
        String content,

        @OptionalURL
        String image
) {
}
