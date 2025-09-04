package br.com.bcfinances.location.domain.contracts;

import br.com.bcfinances.location.domain.entities.State;
import java.util.List;

public interface StateRepository {
    
    List<State> findAll();
}