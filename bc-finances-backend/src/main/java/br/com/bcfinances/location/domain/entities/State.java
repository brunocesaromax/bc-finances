package br.com.bcfinances.location.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class State {

    private Long id;
    private String name;

    public State(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}