package br.com.bcfinances.auth.domain.services;

import br.com.bcfinances.auth.domain.entities.User;

public interface JwtService {
    
    String generateToken(User user);
    Long getExpirationTime();
}