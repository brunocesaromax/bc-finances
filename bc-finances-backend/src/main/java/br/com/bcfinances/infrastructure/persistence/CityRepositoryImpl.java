package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.City;
import br.com.bcfinances.domain.entities.State;
import br.com.bcfinances.domain.repositories.CityRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

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