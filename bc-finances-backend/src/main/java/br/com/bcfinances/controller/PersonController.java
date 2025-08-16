package br.com.bcfinances.controller;

import br.com.bcfinances.event.ResourceCreatedEvent;
import br.com.bcfinances.exceptionHandler.TransactionExceptionHandler;
import br.com.bcfinances.model.Person;
import br.com.bcfinances.repository.PersonRepository;
import br.com.bcfinances.service.PersonService;
import br.com.bcfinances.service.exception.PersonExistentInTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;
    private final ApplicationEventPublisher publisher;
    private final PersonService personService;
    private final MessageSource messageSource;

    @GetMapping(params = "pagination")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON') and #oauth2.hasScope('read')")
    public Page<Person> pagination(@RequestParam(required = false) String name,
                                   Pageable pageable) {
        return personService.pagination(name, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON') and #oauth2.hasScope('read')")
    public List<Person> list() {
        return personRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERSON') and #oauth2.hasScope('write')")
    public ResponseEntity<Person> save(@Valid @RequestBody Person person, HttpServletResponse response) {
        Person personSave = personService.save(person);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, personSave.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(personSave);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_PERSON') and #oauth2.hasScope('read')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.isPresent() ? ResponseEntity.ok(person.get()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Sucesso porém sem conteúdo
    @PreAuthorize("hasAuthority('ROLE_REMOVE_PERSON') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long id) {
        personService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON') and #oauth2.hasScope('write')")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person person) {
        Person personBD = personService.update(id, person);
        return ResponseEntity.ok(personBD);
    }

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON') and #oauth2.hasScope('write')")
    public void updateFieldActive(@PathVariable Long id, @RequestBody Boolean value) {
        personService.updateFieldActive(id, value);
    }

    @ExceptionHandler({PersonExistentInTransactionException.class})
    public ResponseEntity<Object> handlePersonExistentInTransactionException(PersonExistentInTransactionException ex) {
        String msgUser = messageSource.getMessage("person.existent.in.transaction", null, LocaleContextHolder.getLocale());
        String msgDev = Optional.ofNullable(ex.getCause()).isPresent() ? ex.getCause().toString() : ex.toString();
        List<TransactionExceptionHandler.Error> errors = Collections.singletonList(new TransactionExceptionHandler.Error(msgUser, msgDev));

        return ResponseEntity.badRequest().body(errors);
    }
}
