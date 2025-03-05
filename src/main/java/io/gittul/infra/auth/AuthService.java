package io.gittul.infra.auth;

import io.gittul.domain.user.UserRepository;
import io.gittul.domain.user.entity.Role;
import io.gittul.domain.user.entity.User;
import io.gittul.domain.user.exception.UserException;
import io.gittul.infra.auth.dto.LoginRequest;
import io.gittul.infra.auth.dto.SignupRequest;
import io.gittul.infra.auth.exception.AuthenticationException;
import io.gittul.infra.auth.jwt.JwtProvider;
import io.gittul.infra.auth.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> AuthenticationException.USER_NOT_FOUND);

        if (user.getRole().equals(Role.ADMIN)) throw new AuthenticationException("관리자용 API 를 통해 로그인해 주세요");

        if (!user.getPassword().matches(loginRequest.password())) {
            throw AuthenticationException.WRONG_PASSWORD;
        }

        return jwtProvider.createToken(TokenUserInfo.of(user));
    }

    public void signup(SignupRequest signupRequest) {
        if (userRepository.existsByUserName(signupRequest.userName())) {
            throw UserException.USERNAME_ALREADY_EXISTS;
        }

        if (userRepository.existsByEmail(signupRequest.email())) {
            throw UserException.EMAIL_ALREADY_EXISTS;
        }

        User user = User.ofNormal(
                signupRequest.userName(),
                signupRequest.email(),
                "",  // Todo. 이미지 추가
                signupRequest.password()
        );

        userRepository.save(user);
    }
}
