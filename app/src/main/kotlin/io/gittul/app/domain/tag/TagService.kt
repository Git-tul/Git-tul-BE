package io.gittul.app.domain.tag

import io.gittul.core.domain.tag.entity.Tag
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TagService(
    private val tagRepository: TagRepository
) {

    /**
     * 그 이름의 태그가 존재하면 가져오고, 없으면 등록한다.
     *
     * @param tagNames 태그 이름 리스트
     * @return 추가할 전체 태그
     */
    fun getOrCreate(tagNames: List<String>): List<Tag> {
        val existingTags: MutableSet<Tag> = tagRepository.findAllByTagNameIn(tagNames)

        val existingTagNames = existingTags.stream()
            .map { it.tagName }
            .collect(Collectors.toSet())

        val newTags = tagNames.stream()
            .filter { !existingTagNames.contains(it) }
            .distinct()
            .map { tagName: String -> Tag.of(tagName) }
            .toList()

        tagRepository.saveAllAndFlush(newTags)

        existingTags.addAll(newTags)
        return ArrayList(existingTags)
    }
}
