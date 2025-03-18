package io.gittul.app.domain.user

import io.gittul.app.domain.tag.TagService
import io.gittul.core.domain.tag.entity.Tag
import io.gittul.core.domain.tag.entity.UserInterest
import io.gittul.core.domain.user.entity.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tagService: TagService
) {

    @Transactional
    fun interest(
        user: User,
        tagNames: List<String>
    ) {
        val interests: MutableList<UserInterest> = user.details.interests

        val tags: List<Tag> = tagService.getOrCreate(tagNames)

        val newInterests: List<UserInterest> = tags.stream()
            .map<UserInterest> { UserInterest(user, it) }
            .filter { interests.contains(it) }
            .toList()

        interests.addAll(newInterests)
        userRepository.save(user)
    }

    fun unInterest(user: User, tagName: String) {
        user.details.interests
            .removeIf { it.tag.tagName == tagName }
        userRepository.save(user)
    }
}
