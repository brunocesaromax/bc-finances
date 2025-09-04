package br.com.bcfinances.location.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StateJpaRepository extends JpaRepository<StateEntity, Long> {
}