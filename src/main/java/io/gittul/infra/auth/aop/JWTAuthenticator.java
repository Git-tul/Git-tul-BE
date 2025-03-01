package io.gittul.infra.auth.aop;

import io.gittul.infra.auth.exception.AuthenticationException;
import io.gittul.infra.auth.jwt.JwtProvider;
import io.gittul.infra.auth.jwt.TokenUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTAuthenticator implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class) &&
                parameter.getParameterType().isAssignableFrom(TokenUserInfo.class);
    }

    @Override
    public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        String authorizationHeader = request.getHeader("Authorization");
        String token = extractToken(authorizationHeader);
        System.out.println("Extracted token: " + token); // 디버깅용

        return jwtProvider.validateToken(token);
    }

    private String extractToken(String token) {
        if (token == null) throw AuthenticationException.TOKEN_NOT_FOUND;
        if (!token.startsWith("Bearer ")) throw AuthenticationException.INVALID_GRANT_TYPE;

        return token.substring(7);
    }
}
