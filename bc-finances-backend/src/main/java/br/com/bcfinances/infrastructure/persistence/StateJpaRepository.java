package br.com.bcfinances.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StateJpaRepository extends JpaRepository<StateEntity, Long> {
}