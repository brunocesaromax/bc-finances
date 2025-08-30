package br.com.bcfinances.presentation.controllers;

import br.com.bcfinances.application.dto.CityResponse;
import br.com.bcfinances.application.mappers.CityMapper;
import br.com.bcfinances.application.usecases.FindCitiesByStateIdUseCase;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final FindCitiesByStateIdUseCase findCitiesByStateIdUseCase;
    private final CityMapper cityMapper;

    public CityController(FindCitiesByStateIdUseCase findCitiesByStateIdUseCase, CityMapper cityMapper) {
        this.findCitiesByStateIdUseCase = findCitiesByStateIdUseCase;
        this.cityMapper = cityMapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CityResponse> search(@RequestParam Long stateId) {
        return cityMapper.toResponseList(findCitiesByStateIdUseCase.execute(stateId));
    }
}