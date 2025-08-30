package br.com.bcfinances.domain.services;

import br.com.bcfinances.domain.entities.User;

public interface JwtService {
    
    String generateToken(User user);
    Long getExpirationTime();
}