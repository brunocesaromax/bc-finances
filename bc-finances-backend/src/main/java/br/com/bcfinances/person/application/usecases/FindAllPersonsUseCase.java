package br.com.bcfinances.person.application.usecases;

import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.contracts.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindAllPersonsUseCase {

    private final PersonRepository personRepository;

    public FindAllPersonsUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> execute() {
        return personRepository.findAll();
    }
}