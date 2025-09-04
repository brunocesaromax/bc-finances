package br.com.bcfinances.auth.domain.services;

import br.com.bcfinances.auth.domain.entities.User;

public interface AuthenticationService {
    
    User authenticate(String email, String password);
}