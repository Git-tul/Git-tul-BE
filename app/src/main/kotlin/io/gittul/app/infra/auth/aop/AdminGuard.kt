package io.gittul.app.infra.auth.aop

import io.gittul.app.infra.auth.exception.AuthorizationException
import io.gittul.app.infra.auth.jwt.JwtAuthenticator
import io.gittul.core.domain.user.entity.Role
import lombok.RequiredArgsConstructor
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class AdminGuard(
    private val authenticator: JwtAuthenticator
) {

    @Pointcut("@annotation(io.gittul.app.infra.auth.aop.Admin)")
    fun adminMethods() {
    }

    @Before("adminMethods()")
    fun checkAdminRole() {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val user = authenticator.authenticate(request)

        if (user.role != Role.ADMIN) {
            throw AuthorizationException.ADMIN_ONLY
        }
    }
}
