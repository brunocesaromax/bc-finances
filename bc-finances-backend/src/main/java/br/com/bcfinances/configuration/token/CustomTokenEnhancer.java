package br.com.bcfinances.configuration.token;

import br.com.bcfinances.security.UserSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenEnhancer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserSession) {
            UserSession userSession = (UserSession) authentication.getPrincipal();
            
            context.getClaims().claim("name", userSession.getUser().getName());
            context.getClaims().claim("user_id", userSession.getUser().getId());
        }
    }
}