package io.gittul.app.domain.user

import io.gittul.app.infra.auth.jwt.TokenUserInfo
import io.gittul.core.domain.user.entity.User
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class UserInquiryService (
    private val userRepository: UserRepository
){

    fun getUserFromTokenInfo(userInfo: TokenUserInfo): User {
        return userRepository.findById(userInfo.userId)
            .orElseThrow(Supplier { IllegalArgumentException("유저를 찾을 수 없습니다.") })
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow(Supplier { IllegalArgumentException("유저를 찾을 수 없습니다.") })
    }
}
