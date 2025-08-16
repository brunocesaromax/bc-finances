package br.com.bcfinances.controller;

import br.com.bcfinances.model.State;
import br.com.bcfinances.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    private final StateRepository stateRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<State> list() {
        return stateRepository.findAll();
    }

}
