package io.gittul.infra.ai.summery.dto;

import io.gittul.domain.github.api.dto.RepositoryInfo;
import io.gittul.domain.tag.entity.Tag;

import java.util.List;

public record RepositorySummary(
        RepositoryInfo originalInfo,
        String title,
        String description,
        List<Tag> tags
) {
}
