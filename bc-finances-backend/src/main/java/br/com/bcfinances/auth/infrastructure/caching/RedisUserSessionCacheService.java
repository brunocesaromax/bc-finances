package br.com.bcfinances.auth.infrastructure.caching;

import br.com.bcfinances.auth.domain.services.UserSessionCacheService;
import br.com.bcfinances.auth.domain.valueobjects.AuthSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RedisUserSessionCacheService implements UserSessionCacheService {

    private static final String KEY_PREFIX = "bcf:v2:auth:sessions:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;

    public RedisUserSessionCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void store(AuthSession session, Duration timeToLive) {
        valueOperations.set(buildKey(session.getId()), session, timeToLive);
    }

    @Override
    public Optional<AuthSession> findById(String sessionId) {
        Object cached = valueOperations.get(buildKey(sessionId));
        return switch (cached) {
            case AuthSession authSession -> Optional.of(authSession);
            case Map<?, ?> map -> mapToAuthSession(sessionId, map);
            case null, default -> Optional.empty();
        };
    }

    @Override
    public void delete(String sessionId) {
        redisTemplate.delete(buildKey(sessionId));
    }

    private String buildKey(String sessionId) {
        return KEY_PREFIX + sessionId;
    }

    private Optional<AuthSession> mapToAuthSession(String sessionId, Map<?, ?> map) {
        Object userId = map.get("userId");
        Object email = map.get("email");
        Object name = map.get("name");
        Object authorities = map.get("authorities");
        Object issuedAt = map.get("issuedAt");
        Object expiresAt = map.get("expiresAt");

        try {
            Long parsedUserId = userId instanceof Number number ? number.longValue() : null;
            String parsedEmail = email instanceof String emailValue ? emailValue : null;
            String parsedName = name instanceof String nameValue ? nameValue : null;
            Instant parsedIssuedAt = issuedAt instanceof String issuedAtValue ? Instant.parse(issuedAtValue) : null;
            Instant parsedExpiresAt = expiresAt instanceof String expiresAtValue ? Instant.parse(expiresAtValue) : null;
            List<String> parsedAuthorities = authorities instanceof Collection<?> collection
                    ? collection.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toList()
                    : List.of();

            if (parsedIssuedAt == null || parsedExpiresAt == null) {
                return Optional.empty();
            }

            AuthSession session = new AuthSession(
                    sessionId,
                    parsedUserId,
                    parsedEmail,
                    parsedName,
                    parsedAuthorities,
                    parsedIssuedAt,
                    parsedExpiresAt
            );
            return Optional.of(session);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
