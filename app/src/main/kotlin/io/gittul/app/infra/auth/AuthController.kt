package io.gittul.app.infra.auth

import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.LoginSuccessResponse
import io.gittul.app.infra.auth.dto.SignupRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): LoginSuccessResponse {
        return LoginSuccessResponse(authService.login(loginRequest))
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<String> {
        authService.signup(signupRequest)
        return ResponseEntity.ok("회원가입 성공")
    }
}
