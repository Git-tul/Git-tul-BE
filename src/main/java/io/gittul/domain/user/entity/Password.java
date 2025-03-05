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
        System.out.println("password : " + password);
        System.out.println("password-hashed: " + this.password);
    }

    public boolean matches(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        System.out.println("try-to-password : " + password);
        System.out.println("password-selected: " + this.password);

        return BCrypt.checkpw(password, this.password);
    }
}
