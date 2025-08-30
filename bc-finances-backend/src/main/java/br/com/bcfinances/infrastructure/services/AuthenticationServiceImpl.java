package br.com.bcfinances.infrastructure.services;

import br.com.bcfinances.domain.entities.User;
import br.com.bcfinances.domain.services.AuthenticationService;
import br.com.bcfinances.security.UserSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            if (userDetails instanceof UserSession userSession) {
                return userSession.getUser();
            }
            
            throw new IllegalArgumentException("UserDetails must be of type UserSession");
            
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid credentials", ex);
        }
    }
}