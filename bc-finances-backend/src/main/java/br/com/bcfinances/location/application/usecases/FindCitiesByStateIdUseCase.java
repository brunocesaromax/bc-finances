package br.com.bcfinances.location.application.usecases;

import br.com.bcfinances.location.domain.entities.City;
import br.com.bcfinances.location.domain.contracts.CityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindCitiesByStateIdUseCase {

    private final CityRepository cityRepository;

    public FindCitiesByStateIdUseCase(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> execute(Long stateId) {
        return cityRepository.findByStateId(stateId);
    }
}