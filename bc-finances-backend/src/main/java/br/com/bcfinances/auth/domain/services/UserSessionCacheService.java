package br.com.bcfinances.auth.domain.services;

import br.com.bcfinances.auth.domain.valueobjects.AuthSession;

import java.time.Duration;
import java.util.Optional;

public interface UserSessionCacheService {

    void store(AuthSession session, Duration timeToLive);

    Optional<AuthSession> findById(String sessionId);

    void delete(String sessionId);

    default boolean isActive(String sessionId) {
        return findById(sessionId).isPresent();
    }
}
