package io.gittul.app.infra.auth

import io.gittul.app.infra.auth.dto.LoginRequest
import io.gittul.app.infra.auth.dto.SignupRequest
import io.gittul.app.infra.auth.oauth.OauthService
import io.gittul.app.infra.auth.oauth.provider.OauthProviderName
import io.gittul.app.infra.auth.response.AuthResponseHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val oauthService: OauthService,
    private val authResponseHandler: AuthResponseHandler
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        @RequestParam(name = "response", required = false, defaultValue = "body") responseType: String,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        val token = authService.login(loginRequest)
        return authResponseHandler.handleResponse(token, responseType, response)
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<String> {
        authService.signup(signupRequest)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestParam("refreshToken") refreshToken: String,
        @RequestParam(name = "response", required = false, defaultValue = "body") responseType: String,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        val token = authService.refreshToken(refreshToken)
        return authResponseHandler.handleResponse(token, responseType, response)
    }

//    @GetMapping("/oauth/{provider}")
//    fun getOauthAuthorizationUrl(
//        @PathVariable provider: String,
//        @RequestParam(name = "response", required = false, defaultValue = "body") responseType: String
//    ): ResponseEntity<String> {
//        val providerName = OauthProviderName.fromString(provider)
//        val url = oauthService.getAuthorizationUrl(providerName, responseType)
//        return ResponseEntity.ok(url)
//    }

//    @PostMapping("/oauth/{provider}")
//    fun loginWithOauth(
//        @PathVariable provider: String,
//        @RequestParam("code") code: String,
//        @RequestParam(name = "response", required = false, defaultValue = "body") responseType: String,
//        request: HttpServletRequest,
//        response: HttpServletResponse
//    ): ResponseEntity<*> {
//        val providerName = OauthProviderName.fromString(provider)
//        val origin = request.getHeader("Origin")
//        val token = oauthService.login(providerName, code, responseType)
//        return authResponseHandler.handleResponse(token, responseType, response)
//    }
}
