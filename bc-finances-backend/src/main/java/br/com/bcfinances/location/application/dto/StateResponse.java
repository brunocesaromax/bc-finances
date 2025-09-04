package br.com.bcfinances.location.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StateResponse {

    private Long id;
    private String name;

    public StateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}