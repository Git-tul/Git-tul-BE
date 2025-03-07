package io.gittul.domain.post.dto;

import io.gittul.global.validation.OptionalURL;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NormalPostCreateRequest(
        @Size(min = 1, max = 30)
        String title,

        @Size(min = 1, max = 200)
        String content,

        @OptionalURL
        String image,

        List<String> tags
) {
}
