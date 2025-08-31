package br.com.bcfinances.auth.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByEmail(String email);
    
    @Query("SELECT u FROM UserEntity u JOIN u.permissions p WHERE p.description = :permissionDescription")
    List<UserEntity> findByPermissionsDescription(@Param("permissionDescription") String permissionDescription);
}