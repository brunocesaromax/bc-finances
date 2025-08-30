package br.com.bcfinances.infrastructure.services;

import br.com.bcfinances.domain.entities.User;
import br.com.bcfinances.domain.services.JwtService;
import br.com.bcfinances.domain.valueobjects.Permission;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();
        
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
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("authorities", authorities)
                .claim("name", user.getName())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public Long getExpirationTime() {
        return 30 * 60L; // 30 minutos em segundos
    }
}