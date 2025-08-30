package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.City;
import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.entities.State;
import br.com.bcfinances.domain.repositories.PersonRepository;
import br.com.bcfinances.domain.valueobjects.Address;
import br.com.bcfinances.domain.valueobjects.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final PersonJpaRepository personJpaRepository;

    public PersonRepositoryImpl(PersonJpaRepository personJpaRepository) {
        this.personJpaRepository = personJpaRepository;
    }

    @Override
    public List<Person> findAll() {
        return personJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public Person save(Person person) {
        PersonEntity personEntity = toJpaEntity(person);
        PersonEntity savedEntity = personJpaRepository.save(personEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        personJpaRepository.deleteById(id);
    }

    @Override
    public PagedResult<Person> findAllByName(String name, int page, int size) {
        Page<PersonEntity> pagedResult = personJpaRepository.findAllByName(name, PageRequest.of(page, size));
        
        List<Person> persons = pagedResult.getContent().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
        
        return new PagedResult<>(persons, pagedResult.getTotalElements(), page, size);
    }

    private Person toDomainEntity(PersonEntity personEntity) {
        Address address = null;
        if (personEntity.getAddress() != null) {
            address = toDomainAddress(personEntity.getAddress());
        }

        List<Contact> contacts = null;
        if (personEntity.getContacts() != null) {
            contacts = personEntity.getContacts().stream()
                    .map(this::toDomainContact)
                    .collect(Collectors.toList());
        }

        return new Person(
            personEntity.getId(),
            personEntity.getName(),
            personEntity.getActive(),
            address,
            contacts
        );
    }

    private PersonEntity toJpaEntity(Person person) {
        AddressEntity addressEntity = null;
        if (person.getAddress() != null) {
            addressEntity = toJpaAddress(person.getAddress());
        }

        List<ContactEntity> contactEntities = null;
        if (person.getContacts() != null) {
            contactEntities = person.getContacts().stream()
                    .map(contact -> toJpaContact(contact, null))
                    .collect(Collectors.toList());
        }

        PersonEntity personEntity = new PersonEntity(
            person.getId(),
            person.getName(),
            person.getActive(),
            addressEntity,
            contactEntities
        );

        // Set bidirectional relationship for contacts
        if (contactEntities != null) {
            contactEntities.forEach(contact -> contact.setPerson(personEntity));
        }

        return personEntity;
    }

    private Address toDomainAddress(AddressEntity addressEntity) {
        City city = null;
        if (addressEntity.getCity() != null) {
            State state = null;
            if (addressEntity.getCity().getState() != null) {
                state = new State(
                    addressEntity.getCity().getState().getId(),
                    addressEntity.getCity().getState().getName()
                );
            }
            city = new City(
                addressEntity.getCity().getId(),
                addressEntity.getCity().getName(),
                state
            );
        }

        return new Address(
            addressEntity.getStreet(),
            addressEntity.getNumber(),
            addressEntity.getComplement(),
            addressEntity.getNeighborhood(),
            addressEntity.getZipCode(),
            city
        );
    }

    private Contact toDomainContact(ContactEntity contactEntity) {
        return new Contact(
            contactEntity.getId(),
            contactEntity.getName(),
            contactEntity.getEmail(),
            contactEntity.getPhone()
        );
    }

    private AddressEntity toJpaAddress(Address address) {
        CityEntity cityEntity = null;
        if (address.getCity() != null) {
            StateEntity stateEntity = null;
            if (address.getCity().getState() != null) {
                stateEntity = new StateEntity(
                    address.getCity().getState().getId(),
                    address.getCity().getState().getName()
                );
            }
            cityEntity = new CityEntity(
                address.getCity().getId(),
                address.getCity().getName(),
                stateEntity
            );
        }

        return new AddressEntity(
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getZipCode(),
            cityEntity
        );
    }

    private ContactEntity toJpaContact(Contact contact, PersonEntity personEntity) {
        return new ContactEntity(
            contact.getId(),
            contact.getName(),
            contact.getEmail(),
            contact.getPhone(),
            personEntity
        );
    }
}