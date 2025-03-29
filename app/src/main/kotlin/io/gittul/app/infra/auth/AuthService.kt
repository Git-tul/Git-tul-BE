package io.gittul.app.infra.auth

import io.gittul.app.domain.user.UserRepository
import io.gittul.app.domain.user.exception.UserException
import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.SignupRequest
import io.gittul.app.infra.auth.exception.AuthenticationException
import io.gittul.app.infra.auth.jwt.JwtProvider
import io.gittul.app.infra.auth.jwt.TokenUserInfo
import io.gittul.app.infra.auth.oauth.provider.OauthProvider
import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import io.gittul.core.domain.user.entity.OauthInfo
import io.gittul.core.domain.user.entity.Role
import io.gittul.core.domain.user.entity.User
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val oauthProviderList: List<OauthProvider>
) {

    private val oauthProviders: Map<OauthProviderName, OauthProvider> =
        oauthProviderList.associateBy { it.providerName }

    fun login(loginRequest: LoginRequest): String {
        val user: User = userRepository.findByEmail(loginRequest.email)
            ?: throw AuthenticationException.USER_NOT_FOUND

        if (user.role == Role.ADMIN) throw AuthenticationException(
            "관리자용 API 를 통해 로그인해 주세요"
        )

        if (user.oauthInfo != null) {
            throw AuthenticationException(
                "OAuth 계정으로 가입된 이메일입니다. ${user.oauthInfo?.oauthProvider} 계정으로 로그인 해주세요."
            )
        }

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

    fun loginWithOauth(
        provider: OauthProviderName,
        code: String,
        origin: String
    ): String {
        val userInfo = this.oauthProviders[provider]?.getUserInfo(code, origin)
            ?: throw AuthenticationException("OAuth 인증 실패")

        val authInfo = OauthInfo(provider.name, userInfo.oauthId)
        val existingOauthUser = userRepository.findByOauthInfo(authInfo)

        if (existingOauthUser != null) {
            return jwtProvider.createToken(TokenUserInfo.of(existingOauthUser))
        }

        if (userInfo.email != null) {
            val existingUser = userRepository.findByEmail(userInfo.email)

            if (existingUser != null) {
                val providerName = existingUser.oauthInfo?.oauthProvider ?: "이메일"
                throw AuthenticationException(
                    "이미 가입된 이메일입니다. $providerName 계정으로 로그인 해주세요." // Todo. 계정 연결
                )
            }
        }

        var name = userInfo.name
        if (userRepository.existsByUserName(userInfo.name)) {
            name = provider.name + "에서온 " + userInfo.name
        }

        val user = userRepository.save(
            User.ofOauth(
                name,
                userInfo.email,
                userInfo.profileImageUrl,
                authInfo
            )
        )

        return jwtProvider.createToken(TokenUserInfo.of(user))
    }
}
