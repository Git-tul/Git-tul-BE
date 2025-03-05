package io.gittul.infra.auth.jwt;

import io.gittul.domain.user.UserInquiryService;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator {
    private final JwtProvider jwtProvider;
    private final UserInquiryService userService; // Todo. ThreadLocal 로 변경 고려

    public User authenticate(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = extractToken(authorizationHeader);

        TokenUserInfo tokenUserInfo = jwtProvider.validateToken(token);
        return userService.getUserFromTokenInfo(tokenUserInfo);
    }

    private String extractToken(String token) {
        if (token == null) throw AuthenticationException.TOKEN_NOT_FOUND;
        if (!token.startsWith("Bearer ")) throw AuthenticationException.INVALID_GRANT_TYPE;

        return token.substring(7);
    }
}
