package io.gittul.global.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public abstract class PageUtil {

    /**
     * List를 Page로 변환
     * 정렬은 지원하지 않음
     * @param list
     * @param pageRequest
     * @return
     * @param <T>
     */
    public static <T> Page<T> toPage(List<T> list, PageRequest pageRequest) {
        if (pageRequest == null) return new PageImpl<>(list);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
    }
}
