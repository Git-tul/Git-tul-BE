package io.gittul.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

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
}
