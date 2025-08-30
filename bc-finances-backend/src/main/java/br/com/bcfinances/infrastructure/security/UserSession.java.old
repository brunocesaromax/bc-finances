package br.com.bcfinances.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public class UserSession extends User {

    @Serial
    private static final long serialVersionUID = -8081779980837574807L;

    @Getter
    private br.com.bcfinances.model.User user;

    public UserSession(br.com.bcfinances.model.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }
}
