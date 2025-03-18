package io.gittul.app.infra.auth.aop

import io.gittul.app.infra.auth.jwt.JwtAuthenticator
import io.gittul.core.domain.user.entity.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@Component
class AuthenticationArgumentResolver(
    private val authenticator: JwtAuthenticator
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation<Authenticated>(Authenticated::class.java) &&
                parameter.getParameterType().isAssignableFrom(User::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {
        val request = Objects.requireNonNull<HttpServletRequest>(
            webRequest.getNativeRequest<HttpServletRequest>(HttpServletRequest::class.java)
        )
        return authenticator.authenticate(request)
    }
}
