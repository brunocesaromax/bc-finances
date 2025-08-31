package br.com.bcfinances.location.infrastructure.persistence;

import br.com.bcfinances.location.domain.contracts.CityRepository;
import br.com.bcfinances.location.domain.entities.City;
import br.com.bcfinances.location.domain.entities.State;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    private final CityJpaRepository cityJpaRepository;

    public CityRepositoryImpl(CityJpaRepository cityJpaRepository) {
        this.cityJpaRepository = cityJpaRepository;
    }

    @Override
    public List<City> findByStateId(Long stateId) {
        return cityJpaRepository.findByStateId(stateId).stream()
                .map(this::toDomainEntity)
                .toList();
    }

    private City toDomainEntity(CityEntity cityEntity) {
        State state = null;
        if (cityEntity.getState() != null) {
            state = new State(cityEntity.getState().getId(), cityEntity.getState().getName());
        }
        return new City(cityEntity.getId(), cityEntity.getName(), state);
    }
}