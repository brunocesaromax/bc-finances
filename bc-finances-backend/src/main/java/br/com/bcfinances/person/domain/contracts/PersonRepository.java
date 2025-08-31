package br.com.bcfinances.person.domain.contracts;

import br.com.bcfinances.person.domain.entities.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    
    List<Person> findAll();
    Optional<Person> findById(Long id);
    Person save(Person person);
    void deleteById(Long id);
    
    // Pagination support - will need to be handled by specific implementation
    PagedResult<Person> findAllByName(String name, int page, int size);

    record PagedResult<T>(List<T> content, long totalElements, int page, int size) { }
}