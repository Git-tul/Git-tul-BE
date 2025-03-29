package io.gittul.app.domain.user

import io.gittul.core.domain.user.entity.OauthInfo
import io.gittul.core.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByUserName(name: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByOauthInfo(oauthInfo: OauthInfo): User?
}
