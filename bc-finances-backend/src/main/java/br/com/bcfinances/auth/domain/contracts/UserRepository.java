package br.com.bcfinances.auth.domain.contracts;

import br.com.bcfinances.auth.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findByPermissionsDescription(String permissionDescription);
}