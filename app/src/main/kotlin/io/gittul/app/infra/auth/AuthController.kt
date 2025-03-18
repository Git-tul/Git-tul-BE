package io.gittul.app.infra.auth

import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.LoginSuccessResponse
import io.gittul.app.infra.auth.dto.SignupRequest
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
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
    fun login(@RequestBody loginRequest: @Valid LoginRequest): LoginSuccessResponse {
        return LoginSuccessResponse(authService.login(loginRequest))
    }

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: @Valid SignupRequest): ResponseEntity<String> {
        authService.signup(signupRequest)
        return ResponseEntity.ok<String?>("회원가입 성공")
    }
}
