package io.gittul.domain.tag;

import io.gittul.domain.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    /**
     * 그 이름의 태그가 존재하면 가져오고, 없으면 등록한다.
     *
     * @param tagNames 태그 이름 리스트
     * @return 추가할 전체 태그
     */
    public List<Tag> getOrCreate(List<String> tagNames) {

        Set<Tag> existingTags = tagRepository.findAllByTagNameIn(tagNames);

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .map(Tag::of)
                .collect(Collectors.toList());

        tagRepository.saveAll(newTags);

        existingTags.addAll(newTags);
        return new ArrayList<>(existingTags);
    }
}
