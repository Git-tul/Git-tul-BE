package io.gittul.app.infra.auth.aop

import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.core.domain.user.entity.User

object AuthContext {
    private val userHolder: ThreadLocal<User?> = ThreadLocal()

    internal fun setUser(user: User) {
        userHolder.set(user)
    }

    fun getUser(): User {
        return userHolder.get() ?: throw AuthenticationException("유저 정보가 없습니다.")
    }

    internal fun clear() {
        userHolder.remove()
    }
}
