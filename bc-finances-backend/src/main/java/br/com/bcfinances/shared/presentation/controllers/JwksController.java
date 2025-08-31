package br.com.bcfinances.shared.presentation.controllers;

import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JWKSource<SecurityContext> jwkSource;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        try {
            JWKMatcher matcher = new JWKMatcher.Builder().build();
            JWKSelector selector = new JWKSelector(matcher);
            JWKSet jwkSet = new JWKSet(jwkSource.get(selector, null));
            return jwkSet.toJSONObject();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter JWK Set", e);
        }
    }
}