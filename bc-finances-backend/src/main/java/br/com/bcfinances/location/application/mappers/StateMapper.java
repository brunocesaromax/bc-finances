package br.com.bcfinances.location.application.mappers;

import br.com.bcfinances.location.application.dto.StateResponse;
import br.com.bcfinances.location.domain.entities.State;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateMapper {

    public StateResponse toResponse(State state) {
        if (state == null) {
            return null;
        }
        return new StateResponse(state.getId(), state.getName());
    }

    public List<StateResponse> toResponseList(List<State> states) {
        return states.stream()
                .map(this::toResponse)
                .toList();
    }
}