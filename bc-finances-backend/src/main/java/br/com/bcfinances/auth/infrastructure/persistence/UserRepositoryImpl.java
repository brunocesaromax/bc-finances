package br.com.bcfinances.auth.infrastructure.persistence;

import br.com.bcfinances.auth.domain.contracts.UserRepository;
import br.com.bcfinances.auth.domain.entities.User;
import br.com.bcfinances.auth.domain.valueobjects.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomainEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<User> findByPermissionsDescription(String permissionDescription) {
        return userJpaRepository.findByPermissionsDescription(permissionDescription)
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    private User toDomainEntity(UserEntity userEntity) {
        List<Permission> permissions = null;
        if (userEntity.getPermissions() != null) {
            permissions = userEntity.getPermissions().stream()
                    .map(this::toDomainPermission)
                    .toList();
        }

        return new User(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getEmail(),
            userEntity.getPassword(),
            permissions
        );
    }

    private Permission toDomainPermission(PermissionEntity permissionEntity) {
        return new Permission(
            permissionEntity.getId(),
            permissionEntity.getDescription()
        );
    }
}