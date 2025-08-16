package br.com.bcfinances.service;

import br.com.bcfinances.model.Person;
import br.com.bcfinances.model.Person_;
import br.com.bcfinances.repository.PersonRepository;
import br.com.bcfinances.service.exception.PersonExistentInTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Transactional
    public Person save(Person person) {
        person.getContacts().forEach(c -> c.setPerson(person));
        return personRepository.save(person);
    }

    @Transactional
    public Person update(Long id, Person person) {
        Optional<Person> personOptional = personRepository.findById(id);

        person.getContacts().forEach(c -> c.setPerson(person));

        if (!personOptional.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        Person personBD = personOptional.get();
        personBD.updateContacts(person.getContacts());

        BeanUtils.copyProperties(person, personBD, Person_.ID, Person_.CONTACTS); //Modo de update
        return personRepository.save(personOptional.get());
    }

    @Transactional
    public void updateFieldActive(Long id, Boolean ativo) {

        Optional<Person> pessoaBD = personRepository.findById(id);

        if (!pessoaBD.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        pessoaBD.get().setActive(ativo);
        personRepository.save(pessoaBD.get());
    }

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        Optional<Person> pessoa = personRepository.findById(id);
        return pessoa.orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<Person> pagination(String name, Pageable pageable) {
        return personRepository.findAllByName(name, pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        if (transactionService.existsWithPersonId(id)) {
            throw new PersonExistentInTransactionException();
        } else {
            personRepository.deleteById(id);
        }
    }
}
