package br.com.bcfinances.location.presentation.controllers;

import br.com.bcfinances.location.application.dto.StateResponse;
import br.com.bcfinances.location.application.mappers.StateMapper;
import br.com.bcfinances.location.application.usecases.FindAllStatesUseCase;
import br.com.bcfinances.location.domain.entities.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StateControllerTest {

    private static final String STATES_URI = "/states";

    private MockMvc mvc;

    @Mock
    private FindAllStatesUseCase findAllStatesUseCase;

    @Mock
    private StateMapper stateMapper;

    @BeforeEach
    void setUp() {
        StateController controller = new StateController(findAllStatesUseCase, stateMapper);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("Tests for list states")
    class ListStates {

        @Test
        @DisplayName("when get list states then return status OK")
        void whenGetList_thenReturnStatusOK() throws Exception {
            List<State> states = List.of();
            List<StateResponse> responses = List.of();

            when(findAllStatesUseCase.execute()).thenReturn(states);
            when(stateMapper.toResponseList(states)).thenReturn(responses);

            mvc.perform(get(STATES_URI)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            verify(findAllStatesUseCase).execute();
            verify(stateMapper).toResponseList(states);
        }
    }
}
