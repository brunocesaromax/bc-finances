package br.com.bcfinances.person.application.usecases;

import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.contracts.PersonRepository;
import br.com.bcfinances.person.domain.exceptions.PersonNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdatePersonUseCase {

    private final PersonRepository personRepository;

    public UpdatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(Long id, Person person) {
        Optional<Person> existingPerson = personRepository.findById(id);
        
        if (existingPerson.isEmpty()) {
            throw new PersonNotFoundException(id);
        }

        Person personToUpdate = existingPerson.get();
        personToUpdate.setName(person.getName());
        personToUpdate.setActive(person.getActive());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.updateContacts(person.getContacts());

        return personRepository.save(personToUpdate);
    }
}