package io.gittul.app.infra.auth

import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.TokenResponse
import io.gittul.app.infra.auth.dto.SignupRequest
import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): TokenResponse {
        return TokenResponse(authService.login(loginRequest))
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<String> {
        authService.signup(signupRequest)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/oauth/{provider}")
    fun loginWithOauth(
        @PathVariable provider: String,
        @RequestParam("code") code: String,
        request: HttpServletRequest
    ): TokenResponse {
        val providerName = OauthProviderName.fromString(provider)
        val origin = request.getHeader("Origin")

        return TokenResponse(authService.loginWithOauth(providerName, code, origin))
    }
}
