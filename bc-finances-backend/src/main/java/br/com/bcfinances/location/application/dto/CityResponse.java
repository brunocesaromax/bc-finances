package br.com.bcfinances.location.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CityResponse {

    private Long id;
    private String name;
    private StateResponse state;

    public CityResponse(Long id, String name, StateResponse state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

}