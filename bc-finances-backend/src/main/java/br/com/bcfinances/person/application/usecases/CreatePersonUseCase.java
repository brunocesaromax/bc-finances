package br.com.bcfinances.person.application.usecases;

import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.contracts.PersonRepository;
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