package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.User;
import br.com.bcfinances.domain.services.AuthenticationService;
import br.com.bcfinances.domain.services.JwtService;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public LoginUseCase(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    public LoginResult execute(String email, String password) {
        User user = authenticationService.authenticate(email, password);
        String token = jwtService.generateToken(user);
        Long expirationTime = jwtService.getExpirationTime();
        
        return new LoginResult(token, "Bearer", expirationTime, user.getName());
    }

    public record LoginResult(String accessToken, String tokenType, Long expiresIn, String userName) {
    }
}