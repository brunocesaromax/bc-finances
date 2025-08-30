package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FindPersonByIdUseCase {

    private final PersonRepository personRepository;

    public FindPersonByIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> execute(Long id) {
        return personRepository.findById(id);
    }
}