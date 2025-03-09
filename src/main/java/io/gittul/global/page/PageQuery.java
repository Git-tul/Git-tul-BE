package io.gittul.global.page;

import org.springframework.data.domain.PageRequest;

public record PageQuery(
        int page,
        int size
) {

    public PageRequest toRequest() {
        return PageRequest.of(page, size);
    }
}
