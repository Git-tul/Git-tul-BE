package io.gittul.infra.auth.aop;

import io.gittul.domain.user.entity.Role;
import io.gittul.domain.user.entity.User;
import io.gittul.infra.auth.exception.AuthorizationException;
import io.gittul.infra.auth.jwt.JwtAuthenticator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminGuard {

    private final JwtAuthenticator authenticator;

    @Pointcut("@annotation(io.gittul.infra.auth.aop.Admin)")
    public void adminMethods() {
    }

    @Before("adminMethods()")
    public void checkAdminRole() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user = authenticator.authenticate(request);

        if (user.getRole() != Role.ADMIN) {
            throw AuthorizationException.ADMIN_ONLY;
        }
    }
}
