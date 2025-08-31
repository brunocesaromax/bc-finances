package br.com.bcfinances.location.domain.contracts;

import br.com.bcfinances.location.domain.entities.City;
import java.util.List;

public interface CityRepository {
    
    List<City> findByStateId(Long stateId);
}