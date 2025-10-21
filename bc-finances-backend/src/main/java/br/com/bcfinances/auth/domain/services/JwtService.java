package br.com.bcfinances.auth.domain.services;

import br.com.bcfinances.auth.domain.entities.User;

import java.time.Instant;

public interface JwtService {

    GeneratedToken generateToken(User user);

    Long getExpirationTime();

    record GeneratedToken(String tokenValue, String sessionId, Instant issuedAt, Instant expiresAt) {
    }
}
