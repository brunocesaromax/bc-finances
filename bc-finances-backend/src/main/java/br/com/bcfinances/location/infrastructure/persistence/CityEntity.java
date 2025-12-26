package br.com.bcfinances.location.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
