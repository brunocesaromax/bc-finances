package br.com.bcfinances.person.application.mappers;

import br.com.bcfinances.person.application.dto.*;
import br.com.bcfinances.person.application.dto.PersonRequest.AddressRequest;
import br.com.bcfinances.person.application.dto.PersonRequest.ContactRequest;
import br.com.bcfinances.location.application.dto.CityResponse;
import br.com.bcfinances.location.domain.entities.City;
import br.com.bcfinances.location.application.mappers.CityMapper;
import br.com.bcfinances.location.application.mappers.StateMapper;
import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.valueobjects.Address;
import br.com.bcfinances.person.domain.valueobjects.Contact;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    private final CityMapper cityMapper;
    private final StateMapper stateMapper;

    public PersonMapper(CityMapper cityMapper, StateMapper stateMapper) {
        this.cityMapper = cityMapper;
        this.stateMapper = stateMapper;
    }

    public PersonResponse toResponse(Person person) {
        if (person == null) {
            return null;
        }

        AddressResponse addressResponse = null;
        if (person.getAddress() != null) {
            addressResponse = toAddressResponse(person.getAddress());
        }

        List<ContactResponse> contactResponses = null;
        if (person.getContacts() != null) {
            contactResponses = person.getContacts().stream()
                    .map(this::toContactResponse)
                    .toList();
        }

        return new PersonResponse(
            person.getId(),
            person.getName(),
            person.getActive(),
            addressResponse,
            contactResponses
        );
    }

    public List<PersonResponse> toResponseList(List<Person> persons) {
        return persons.stream()
                .map(this::toResponse)
                .toList();
    }

    public Person toEntity(PersonRequest personRequest) {
        if (personRequest == null) {
            return null;
        }

        Address address = null;
        if (personRequest.getAddress() != null) {
            address = toAddressEntity(personRequest.getAddress());
        }

        List<Contact> contacts = null;
        if (personRequest.getContacts() != null) {
            contacts = personRequest.getContacts().stream()
                    .map(this::toContactEntity)
                    .toList();
        }

        return new Person(
            null,
            personRequest.getName(),
            personRequest.getActive(),
            address,
            contacts
        );
    }

    private AddressResponse toAddressResponse(Address address) {
        CityResponse cityResponse = null;
        if (address.getCity() != null) {
            cityResponse = cityMapper.toResponse(address.getCity());
        }

        return new AddressResponse(
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getZipCode(),
            cityResponse
        );
    }

    private ContactResponse toContactResponse(Contact contact) {
        return new ContactResponse(
            contact.getId(),
            contact.getName(),
            contact.getEmail(),
            contact.getPhone()
        );
    }

    private Address toAddressEntity(AddressRequest addressRequest) {
        City city = null;
        if (addressRequest.getCityId() != null) {
            city = new City(addressRequest.getCityId(), null, null);
        }

        return new Address(
            addressRequest.getStreet(),
            addressRequest.getNumber(),
            addressRequest.getComplement(),
            addressRequest.getNeighborhood(),
            addressRequest.getZipCode(),
            city
        );
    }

    private Contact toContactEntity(ContactRequest contactRequest) {
        return new Contact(
            contactRequest.getId(),
            contactRequest.getName(),
            contactRequest.getEmail(),
            contactRequest.getPhone()
        );
    }
}