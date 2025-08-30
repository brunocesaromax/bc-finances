package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.State;
import br.com.bcfinances.domain.repositories.StateRepository;
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