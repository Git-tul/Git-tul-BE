package io.gittul.domain.user;

import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInquiryService {
    private final UserRepository userRepository;

    public User getUserFromTokenInfo(TokenUserInfo userInfo) {
        return userRepository.findById(userInfo.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
