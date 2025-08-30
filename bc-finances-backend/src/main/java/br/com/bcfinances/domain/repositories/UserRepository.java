package br.com.bcfinances.domain.repositories;

import br.com.bcfinances.domain.entities.User;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}