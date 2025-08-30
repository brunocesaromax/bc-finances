package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.PersonRepository;
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