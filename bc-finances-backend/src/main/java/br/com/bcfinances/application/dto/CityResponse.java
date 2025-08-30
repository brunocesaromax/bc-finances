package br.com.bcfinances.application.dto;

public class CityResponse {

    private Long id;
    private String name;
    private StateResponse state;

    public CityResponse() {}

    public CityResponse(Long id, String name, StateResponse state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StateResponse getState() {
        return state;
    }

    public void setState(StateResponse state) {
        this.state = state;
    }
}