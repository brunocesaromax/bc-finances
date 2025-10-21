package br.com.bcfinances.shared.infrastructure.services;

import br.com.bcfinances.auth.domain.entities.User;
import br.com.bcfinances.auth.domain.services.JwtService;
import br.com.bcfinances.auth.domain.valueobjects.Permission;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public GeneratedToken generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(getExpirationTime());
        String sessionId = UUID.randomUUID().toString();

        String authorities = user.getPermissions() != null 
            ? user.getPermissions()
                .stream()
                .map(Permission::getAuthority)
                .collect(Collectors.joining(" "))
            : "";

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("bc-finances")
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .id(sessionId)
                .claim("authorities", authorities)
                .claim("name", user.getName())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new GeneratedToken(token, sessionId, now, expiresAt);
    }

    @Override
    public Long getExpirationTime() {
        return 30 * 60L; // 30 minutos em segundos
    }
}
