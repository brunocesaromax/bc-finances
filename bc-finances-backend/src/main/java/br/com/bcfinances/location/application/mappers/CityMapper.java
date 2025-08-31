package br.com.bcfinances.location.application.mappers;

import br.com.bcfinances.location.application.dto.CityResponse;
import br.com.bcfinances.location.domain.entities.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {

    private final StateMapper stateMapper;

    public CityMapper(StateMapper stateMapper) {
        this.stateMapper = stateMapper;
    }

    public CityResponse toResponse(City city) {
        if (city == null) {
            return null;
        }
        return new CityResponse(
            city.getId(), 
            city.getName(), 
            stateMapper.toResponse(city.getState())
        );
    }

    public List<CityResponse> toResponseList(List<City> cities) {
        return cities.stream()
                .map(this::toResponse)
                .toList();
    }
}