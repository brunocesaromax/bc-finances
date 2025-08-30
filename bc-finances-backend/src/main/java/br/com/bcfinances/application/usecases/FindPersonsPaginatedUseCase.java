package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.PersonRepository;
import br.com.bcfinances.domain.repositories.PersonRepository.PagedResult;
import org.springframework.stereotype.Service;

@Service
public class FindPersonsPaginatedUseCase {

    private final PersonRepository personRepository;

    public FindPersonsPaginatedUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PagedResult<Person> execute(String name, int page, int size) {
        return personRepository.findAllByName(name, page, size);
    }
}