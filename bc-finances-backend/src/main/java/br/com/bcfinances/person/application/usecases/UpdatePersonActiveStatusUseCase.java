package br.com.bcfinances.person.application.usecases;

import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.contracts.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdatePersonActiveStatusUseCase {

    private final PersonRepository personRepository;

    public UpdatePersonActiveStatusUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void execute(Long id, Boolean active) {
        Optional<Person> existingPerson = personRepository.findById(id);
        
        if (existingPerson.isEmpty()) {
            throw new br.com.bcfinances.person.domain.exceptions.PersonNotFoundException(id);
        }

        Person person = existingPerson.get();
        person.setActive(active);
        personRepository.save(person);
    }
}