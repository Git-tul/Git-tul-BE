package io.gittul.infra.auth.dto;

import jakarta.validation.constraints.Email;

public record SignupRequest(
        String userName,
        @Email String email,
        String password) {
}
