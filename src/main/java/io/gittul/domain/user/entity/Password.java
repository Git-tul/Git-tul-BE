package io.gittul.domain.user.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Embeddable
@NoArgsConstructor
public class Password {
    private String password;

    public Password(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        return BCrypt.checkpw(password, this.password);
    }
    
    public static final String CONTAINS_SPECIAL_CHAR = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";
    public static final String CONTAINS_UPPERCASE = "^(?=.*[A-Z]).+$";
    public static final String CONTAINS_LOWERCASE = "^(?=.*[a-z]).+$";
    public static final String CONTAINS_DIGIT_REGEX = "^(?=.*\\d).+$";

    @Pattern.List({
            @Pattern(regexp = CONTAINS_SPECIAL_CHAR, message = "비밀번호는 특수문자를 포함해야 합니다."),
            @Pattern(regexp = CONTAINS_UPPERCASE, message = "비밀번호는 대문자를 포함해야 합니다."),
            @Pattern(regexp = CONTAINS_LOWERCASE, message = "비밀번호는 소문자를 포함해야 합니다."),
            @Pattern(regexp = CONTAINS_DIGIT_REGEX, message = "비밀번호는 숫자를 포함해야 합니다.")
    })
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})

    public @interface ValidPassword {
        String message() default "비밀번호가 유효하지 않습니다.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}

