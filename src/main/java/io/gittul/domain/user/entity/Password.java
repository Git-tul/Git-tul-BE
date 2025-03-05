package io.gittul.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Embeddable
@NoArgsConstructor
public class Password {
    private String password;

    public Password(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
