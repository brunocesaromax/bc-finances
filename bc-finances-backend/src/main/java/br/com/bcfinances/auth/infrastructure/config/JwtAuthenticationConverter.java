package br.com.bcfinances.auth.infrastructure.config;

import br.com.bcfinances.auth.domain.services.UserSessionCacheService;
import br.com.bcfinances.auth.domain.valueobjects.AuthSession;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserSessionCacheService userSessionCacheService;

    public JwtAuthenticationConverter(UserSessionCacheService userSessionCacheService) {
        this.userSessionCacheService = userSessionCacheService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        validateSession(jwt);
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // Extrair authorities do claim 'authorities'
        String authoritiesString = jwt.getClaimAsString("authorities");
        
        if (authoritiesString == null || authoritiesString.isEmpty()) {
            return Collections.emptyList();
        }

        return Stream.of(authoritiesString.split(" "))
                .map(String::trim)
                .filter(authority -> !authority.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void validateSession(Jwt jwt) {
        String sessionId = jwt.getId();
        if (sessionId == null || sessionId.isBlank()) {
            throw new BadCredentialsException("Sessão inválida");
        }

        AuthSession session = userSessionCacheService.findById(sessionId)
                .orElseThrow(() -> new BadCredentialsException("Sessão expirada ou inexistente"));

        Instant now = Instant.now();
        if (session.getExpiresAt() != null && now.isAfter(session.getExpiresAt())) {
            userSessionCacheService.delete(sessionId);
            throw new BadCredentialsException("Sessão expirada");
        }
    }
}
