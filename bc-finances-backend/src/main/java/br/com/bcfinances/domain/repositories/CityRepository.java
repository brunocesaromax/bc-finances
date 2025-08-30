package br.com.bcfinances.domain.repositories;

import br.com.bcfinances.domain.entities.City;
import java.util.List;

public interface CityRepository {
    
    List<City> findByStateId(Long stateId);
}