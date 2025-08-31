package br.com.bcfinances.location.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class City {

    private Long id;
    private String name;
    private State state;

    public City(Long id, String name, State state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

}