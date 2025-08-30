package br.com.bcfinances.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "cities")
@Setter
@Getter
public class CityEntity {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity state;

    public CityEntity() {}

    public CityEntity(Long id, String name, StateEntity state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }
}