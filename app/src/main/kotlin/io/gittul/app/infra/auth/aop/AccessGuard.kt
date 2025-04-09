package io.gittul.app.infra.auth.aop

import io.gittul.core.domain.user.entity.Role

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessGuard(
    val role: Role = Role.USER
)
