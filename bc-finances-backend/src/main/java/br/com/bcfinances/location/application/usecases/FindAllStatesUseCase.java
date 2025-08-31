package br.com.bcfinances.location.application.usecases;

import br.com.bcfinances.location.domain.entities.State;
import br.com.bcfinances.location.domain.contracts.StateRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindAllStatesUseCase {

    private final StateRepository stateRepository;

    public FindAllStatesUseCase(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<State> execute() {
        return stateRepository.findAll();
    }
}