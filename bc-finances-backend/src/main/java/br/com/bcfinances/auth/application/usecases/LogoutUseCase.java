package br.com.bcfinances.auth.application.usecases;

import br.com.bcfinances.auth.domain.services.UserSessionCacheService;
import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase {

    private final UserSessionCacheService userSessionCacheService;

    public LogoutUseCase(UserSessionCacheService userSessionCacheService) {
        this.userSessionCacheService = userSessionCacheService;
    }

    public void execute(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return;
        }

        userSessionCacheService.delete(sessionId);
    }
}
