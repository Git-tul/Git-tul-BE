package io.gittul.infra.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gittul.domain.user.exception.UserException;
import io.gittul.infra.auth.dto.LoginRequest;
import io.gittul.infra.auth.dto.SignupRequest;
import io.gittul.infra.auth.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private static final String SIGNUP_URL = "/auth/signup";
    private static final String LOGIN_URL = "/auth/login";
    private static final String LOGOUT_URL = "/auth/logout";

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new io.gittul.global.exception.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("중복된 유저명으로 회원가입 시 409 Conflict 반환")
    @Test
    public void testSignupWithDuplicateUsername() throws Exception {
        SignupRequest signupRequest = new SignupRequest("duplicateUser", "email@example.com", "password");
        doThrow(UserException.USERNAME_ALREADY_EXISTS).when(authService).signup(any(SignupRequest.class));

        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict());
    }

    @DisplayName("중복된 이메일로 회원가입 시 409 Conflict 반환")
    @Test
    public void testSignupWithDuplicateEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username", "duplicate@example.com", "password");
        doThrow(UserException.EMAIL_ALREADY_EXISTS).when(authService).signup(any(SignupRequest.class));

        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict());
    }

    @DisplayName("잘못된 비밀번호로 로그인 시 401 Unauthorized 반환")
    @Test
    public void testLoginWithWrongPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@example.com", "wrongPassword");
        doThrow(AuthenticationException.WRONG_PASSWORD).when(authService).login(any(LoginRequest.class));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 성공 시 인증 헤더에 토큰 포함")
    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        when(authService.login(any(LoginRequest.class))).thenReturn("token");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer token"));
    }

    @DisplayName("존재하지 않는 사용자 로그인 시 401 Unauthorized 반환")
    @Test
    public void testLoginWithNonExistentUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password");
        doThrow(AuthenticationException.USER_NOT_FOUND).when(authService).login(any(LoginRequest.class));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
