package br.com.bcfinances.controller;

import br.com.bcfinances.model.City;
import br.com.bcfinances.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityRepository cityRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<City> search(@RequestParam Long stateId) {
        return cityRepository.findByStateId(stateId);
    }

}
