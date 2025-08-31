package br.com.bcfinances.location.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "states")
@Setter
@Getter
public class StateEntity {

    @Id
    private Long id;

    private String name;

    public StateEntity() {}

    public StateEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}