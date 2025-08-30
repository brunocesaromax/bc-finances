package br.com.bcfinances.infrastructure.security;

import br.com.bcfinances.domain.entities.User;
import br.com.bcfinances.domain.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> 
            new UsernameNotFoundException("Usuário e/ou senha incorretos"));

        return new UserSession(user, getPermissions(user));
    }

    private Collection<? extends GrantedAuthority> getPermissions(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getPermissions() != null) {
            user.getPermissions().forEach(p -> 
                authorities.add(new SimpleGrantedAuthority(p.getAuthority())));
        }
        return authorities;
    }
}