package io.gittul.infra.ai.summery.dto;

import java.util.List;

public record RepositorySummary(
        String title,
        String description,
        List<String> tags
) {
}
