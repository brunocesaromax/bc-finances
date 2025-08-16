package br.com.bcfinances.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserSession extends User {

    private static final long serialVersionUID = 1L;

    @Getter
    private br.com.bcfinances.model.User user;

    public UserSession(br.com.bcfinances.model.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }
}
