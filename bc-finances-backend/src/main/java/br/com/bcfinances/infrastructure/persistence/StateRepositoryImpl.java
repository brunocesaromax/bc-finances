package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.State;
import br.com.bcfinances.domain.repositories.StateRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    private State toDomainEntity(StateEntity stateEntity) {
        return new State(stateEntity.getId(), stateEntity.getName());
    }
}