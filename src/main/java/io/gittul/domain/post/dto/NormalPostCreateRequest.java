package io.gittul.domain.post.dto;

import org.hibernate.validator.constraints.URL;

import java.util.List;

public record NormalPostCreateRequest(
        Long userId,
        @URL String repoUrl,
        String title,
        String content,
        List<String> tags
) {
}
