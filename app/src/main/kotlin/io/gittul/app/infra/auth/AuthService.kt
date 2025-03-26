package io.gittul.app.infra.auth

import io.gittul.app.domain.user.UserRepository
import io.gittul.app.domain.user.exception.UserException
import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.SignupRequest
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.app.infra.auth.jwt.JwtProvider
import io.gittul.app.infra.auth.jwt.TokenUserInfo
import io.gittul.core.domain.user.entity.Role
import io.gittul.core.domain.user.entity.User
import io.gittul.core.global.exception.CustomException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {

    fun login(loginRequest: LoginRequest): String {
        val user: User = userRepository.findByEmail(loginRequest.email)
            ?: throw AuthenticationException.USER_NOT_FOUND

        if (user.role == Role.ADMIN) throw AuthenticationException(
            "관리자용 API 를 통해 로그인해 주세요"
        )

        if (!user.password.matches(loginRequest.password)) {
            throw AuthenticationException.WRONG_PASSWORD
        }

        return jwtProvider.createToken(TokenUserInfo.of(user))
    }

    fun signup(signupRequest: SignupRequest) {
        if (userRepository.existsByUserName(signupRequest.userName)) {
            throw UserException.USERNAME_ALREADY_EXISTS
        }

        if (userRepository.existsByEmail(signupRequest.email)) {
            throw UserException.EMAIL_ALREADY_EXISTS
        }

        val user = User.ofNormal(
            signupRequest.userName,
            signupRequest.email,
            signupRequest.profileImage,
            signupRequest.password
        )

        userRepository.save(user)
    }
}
