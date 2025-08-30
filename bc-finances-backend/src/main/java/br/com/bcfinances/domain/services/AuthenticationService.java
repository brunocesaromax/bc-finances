package br.com.bcfinances.domain.services;

import br.com.bcfinances.domain.entities.User;

public interface AuthenticationService {
    
    User authenticate(String email, String password);
}