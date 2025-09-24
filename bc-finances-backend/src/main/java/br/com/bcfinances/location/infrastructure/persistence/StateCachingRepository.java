package br.com.bcfinances.location.infrastructure.persistence;

import br.com.bcfinances.location.domain.contracts.StateRepository;
import br.com.bcfinances.location.domain.entities.State;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class StateCachingRepository implements StateRepository {

    private final StateRepositoryImpl delegate;

    public StateCachingRepository(StateRepositoryImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    @Cacheable(cacheNames = "states:all")
    public List<State> findAll() {
        return delegate.findAll();
    }
}

