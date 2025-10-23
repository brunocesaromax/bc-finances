package br.com.bcfinances.location.infrastructure.persistence;

import br.com.bcfinances.location.domain.contracts.CityRepository;
import br.com.bcfinances.location.domain.entities.City;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class CityCachingRepository implements CityRepository {

    private final CityRepositoryImpl delegate;

    public CityCachingRepository(CityRepositoryImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    @Cacheable(cacheNames = "cities:byState", key = "#stateId")
    public List<City> findByStateId(Long stateId) {
        return delegate.findByStateId(stateId);
    }
}

