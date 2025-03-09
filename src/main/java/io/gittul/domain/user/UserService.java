package io.gittul.domain.user;

import io.gittul.domain.tag.TagService;
import io.gittul.domain.tag.entity.Tag;
import io.gittul.domain.tag.entity.UserInterest;
import io.gittul.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TagService tagService;

    @Transactional
    public void interest(User user, List<String> tagNames) {
        List<UserInterest> interests = user.getDetails().getInterests();

        List<Tag> tags = tagService.getOrCreate(tagNames);

        List<UserInterest> newInterests = tags.stream()
                .map(tag -> new UserInterest(user, tag))
                .filter(interest -> !interests.contains(interest))
                .toList();

        interests.addAll(newInterests);
        userRepository.save(user);
    }

    public void unInterest(User user, String tagName) {
        user.getDetails().getInterests().removeIf(
                userInterest -> userInterest.getTag().getTagName().equals(tagName)
        );
        userRepository.save(user);
    }
}
