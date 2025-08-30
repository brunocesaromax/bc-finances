package br.com.bcfinances.presentation.controllers;

import br.com.bcfinances.application.dto.PersonRequest;
import br.com.bcfinances.application.dto.PersonResponse;
import br.com.bcfinances.application.mappers.PersonMapper;
import br.com.bcfinances.application.usecases.*;
import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.PersonRepository.PagedResult;
import br.com.bcfinances.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final CreatePersonUseCase createPersonUseCase;
    private final FindAllPersonsUseCase findAllPersonsUseCase;
    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final FindPersonsPaginatedUseCase findPersonsPaginatedUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final UpdatePersonActiveStatusUseCase updatePersonActiveStatusUseCase;
    private final DeletePersonUseCase deletePersonUseCase;
    private final PersonMapper personMapper;
    private final ApplicationEventPublisher publisher;

    public PersonController(CreatePersonUseCase createPersonUseCase,
                           FindAllPersonsUseCase findAllPersonsUseCase,
                           FindPersonByIdUseCase findPersonByIdUseCase,
                           FindPersonsPaginatedUseCase findPersonsPaginatedUseCase,
                           UpdatePersonUseCase updatePersonUseCase,
                           UpdatePersonActiveStatusUseCase updatePersonActiveStatusUseCase,
                           DeletePersonUseCase deletePersonUseCase,
                           PersonMapper personMapper,
                           ApplicationEventPublisher publisher) {
        this.createPersonUseCase = createPersonUseCase;
        this.findAllPersonsUseCase = findAllPersonsUseCase;
        this.findPersonByIdUseCase = findPersonByIdUseCase;
        this.findPersonsPaginatedUseCase = findPersonsPaginatedUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.updatePersonActiveStatusUseCase = updatePersonActiveStatusUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
        this.personMapper = personMapper;
        this.publisher = publisher;
    }

    @GetMapping(params = "pagination")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON')")
    public Page<PersonResponse> pagination(@RequestParam(required = false) String name, Pageable pageable) {
        PagedResult<Person> result = findPersonsPaginatedUseCase.execute(name, pageable.getPageNumber(), pageable.getPageSize());
        
        List<PersonResponse> content = personMapper.toResponseList(result.content());
        return new PageImpl<>(content, pageable, result.totalElements());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON')")
    public List<PersonResponse> list() {
        List<Person> persons = findAllPersonsUseCase.execute();
        return personMapper.toResponseList(persons);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERSON')")
    public ResponseEntity<PersonResponse> save(@Valid @RequestBody PersonRequest personRequest, HttpServletResponse response) {
        Person person = personMapper.toEntity(personRequest);
        Person savedPerson = createPersonUseCase.execute(person);
        PersonResponse personResponse = personMapper.toResponse(savedPerson);
        
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPerson.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(personResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON')")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        Optional<Person> person = findPersonByIdUseCase.execute(id);
        return person.map(p -> ResponseEntity.ok(personMapper.toResponse(p)))
                    .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVE_PERSON')")
    public void delete(@PathVariable Long id) {
        deletePersonUseCase.execute(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON')")
    public ResponseEntity<PersonResponse> update(@PathVariable Long id, @Valid @RequestBody PersonRequest personRequest) {
        Person person = personMapper.toEntity(personRequest);
        Person updatedPerson = updatePersonUseCase.execute(id, person);
        PersonResponse personResponse = personMapper.toResponse(updatedPerson);
        return ResponseEntity.ok(personResponse);
    }

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON')")
    public void updateFieldActive(@PathVariable Long id, @RequestBody Boolean value) {
        updatePersonActiveStatusUseCase.execute(id, value);
    }
}