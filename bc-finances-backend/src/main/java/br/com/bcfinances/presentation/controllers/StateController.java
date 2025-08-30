package br.com.bcfinances.presentation.controllers;

import br.com.bcfinances.application.dto.StateResponse;
import br.com.bcfinances.application.mappers.StateMapper;
import br.com.bcfinances.application.usecases.FindAllStatesUseCase;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {

    private final FindAllStatesUseCase findAllStatesUseCase;
    private final StateMapper stateMapper;

    public StateController(FindAllStatesUseCase findAllStatesUseCase, StateMapper stateMapper) {
        this.findAllStatesUseCase = findAllStatesUseCase;
        this.stateMapper = stateMapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<StateResponse> list() {
        return stateMapper.toResponseList(findAllStatesUseCase.execute());
    }
}