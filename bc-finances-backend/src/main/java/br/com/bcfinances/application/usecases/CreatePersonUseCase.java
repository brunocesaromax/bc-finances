package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePersonUseCase {

    private final PersonRepository personRepository;

    public CreatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(Person person) {
        return personRepository.save(person);
    }
}