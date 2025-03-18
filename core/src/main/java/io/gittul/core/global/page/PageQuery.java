package io.gittul.core.global.page;

import org.springframework.data.domain.PageRequest;

public record PageQuery(
        Integer page,
        Integer size
) {

    public PageRequest toRequest() {
        return PageRequest.of(
                page == null ? 0 : page,
                size == null ? 10 : size);
    }
}
