package io.gittul.app.infra.auth.aop

import io.gittul.app.global.AopAnnotationUtils
import io.gittul.app.infra.auth.exception.AuthorizationException
import io.gittul.app.infra.auth.jwt.JwtAuthenticator
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class AccessGuardAspect(
    private val authenticator: JwtAuthenticator
) {
    @Pointcut("@annotation(io.gittul.app.infra.auth.aop.AccessGuard)")
    fun methodAccessGuard() {
    }

    @Pointcut("@within(io.gittul.app.infra.auth.aop.AccessGuard)")
    fun classAccessGuard() {
    }


    @Before("methodAccessGuard() || classAccessGuard()")
    fun checkMethodAccess(joinPoint: JoinPoint) {
        val annotation = AopAnnotationUtils.getAnnotation(joinPoint, AccessGuard::class.java)
        checkAccess(annotation)
    }

    private fun checkAccess(accessGuard: AccessGuard?) {
        if (accessGuard == null) return

        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val user = authenticator.authenticate(request)

        if (!user.role.includes(accessGuard.role)) {
            throw AuthorizationException.requireRole(accessGuard.role)
        }

        AuthContext.setUser(user)
    }
}
