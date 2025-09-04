package br.com.bcfinances.location.infrastructure.persistence;

import br.com.bcfinances.location.domain.contracts.StateRepository;
import br.com.bcfinances.location.domain.entities.State;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StateRepositoryImpl implements StateRepository {

    private final StateJpaRepository stateJpaRepository;

    public StateRepositoryImpl(StateJpaRepository stateJpaRepository) {
        this.stateJpaRepository = stateJpaRepository;
    }

    @Override
    public List<State> findAll() {
        return stateJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    private State toDomainEntity(StateEntity stateEntity) {
        return new State(stateEntity.getId(), stateEntity.getName());
    }
}