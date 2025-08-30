package br.com.bcfinances.domain.repositories;

import br.com.bcfinances.domain.entities.State;
import java.util.List;

public interface StateRepository {
    
    List<State> findAll();
}