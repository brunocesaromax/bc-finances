package br.com.bcfinances.infrastructure.mappers;

import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.infrastructure.persistence.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonEntityMapper {

    public PersonEntity toEntity(Person domain) {
        if (domain == null) {
            return null;
        }

        PersonEntity entity = new PersonEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setActive(domain.getActive());

        return entity;
    }

    public Person toDomain(PersonEntity entity) {
        if (entity == null) {
            return null;
        }

        Person domain = new Person();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setActive(entity.getActive());

        return domain;
    }
}