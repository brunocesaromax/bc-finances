package br.com.bcfinances.auth.application.usecases;

import br.com.bcfinances.auth.domain.entities.User;
import br.com.bcfinances.auth.domain.services.AuthenticationService;
import br.com.bcfinances.auth.domain.services.JwtService;
import br.com.bcfinances.auth.domain.services.UserSessionCacheService;
import br.com.bcfinances.auth.domain.valueobjects.AuthSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginUseCase {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserSessionCacheService userSessionCacheService;

    public LoginUseCase(AuthenticationService authenticationService,
                        JwtService jwtService,
                        UserSessionCacheService userSessionCacheService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userSessionCacheService = userSessionCacheService;
    }

    public LoginResult execute(String email, String password) {
        User user = authenticationService.authenticate(email, password);
        JwtService.GeneratedToken generatedToken = jwtService.generateToken(user);
        Duration timeToLive = Duration.between(generatedToken.issuedAt(), generatedToken.expiresAt());

        List<String> authorities = user.getPermissions() != null
                ? user.getPermissions().stream()
                    .map(permission -> permission.getAuthority() != null ? permission.getAuthority() : "")
                    .filter(authority -> !authority.isEmpty())
                    .collect(Collectors.toList())
                : List.of();

        AuthSession session = new AuthSession(
                generatedToken.sessionId(),
                user.getId(),
                user.getEmail(),
                user.getName(),
                authorities,
                generatedToken.issuedAt(),
                generatedToken.expiresAt()
        );

        userSessionCacheService.store(session, timeToLive);

        Long expirationTime = jwtService.getExpirationTime();
        
        return new LoginResult(generatedToken.tokenValue(), "Bearer", expirationTime, user.getName());
    }

    public record LoginResult(String accessToken, String tokenType, Long expiresIn, String userName) {
    }
}
