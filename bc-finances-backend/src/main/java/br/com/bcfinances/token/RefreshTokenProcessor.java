package br.com.bcfinances.token;

import br.com.bcfinances.configuration.property.ApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * NOTA: Esta classe foi temporariamente desabilitada durante a migração para Spring Authorization Server.
 * O Spring Authorization Server usa um modelo diferente para tokens e refresh tokens.
 * Esta funcionalidade precisará ser reimplementada usando os novos padrões do Spring Authorization Server.
 */
@Profile("oauth-security")
@ControllerAdvice
public class RefreshTokenProcessor implements ResponseBodyAdvice<Object> {

    @Autowired
    private ApiProperty apiProperty;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Temporariamente desabilitado durante migração para Spring Authorization Server
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                 MethodParameter returnType,
                                 MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request,
                                 ServerHttpResponse response) {

        // Implementação temporariamente desabilitada
        // TODO: Reimplementar usando Spring Authorization Server APIs
        return body;
    }

    // Métodos de utilidade que podem ser reutilizados na nova implementação
    private void addRefreshTokenInCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(apiProperty.getSecurity().isEnableHttps());
        refreshTokenCookie.setPath(req.getContextPath()+"/oauth2/token");
        refreshTokenCookie.setMaxAge(2592000); //30 dias
        resp.addCookie(refreshTokenCookie);
    }
}