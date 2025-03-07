package io.gittul.infra.auth.dto;

import io.gittul.domain.user.entity.Password;
import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email
        String email,

        @Password.ValidPassword
        String password) {
}
