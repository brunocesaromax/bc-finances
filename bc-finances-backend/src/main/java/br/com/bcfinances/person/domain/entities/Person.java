package br.com.bcfinances.person.domain.entities;

import br.com.bcfinances.person.domain.valueobjects.Address;
import br.com.bcfinances.person.domain.valueobjects.Contact;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Person {

    private Long id;
    private String name;
    private Boolean active;
    private Address address;
    private List<Contact> contacts;

    public Person() {}

    public Person(Long id, String name, Boolean active, Address address, List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.address = address;
        this.contacts = contacts;
    }

    public Boolean isInactive() {
        return !this.active;
    }

    public void updateContacts(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts != null ? contacts : java.util.Collections.emptyList());
    }
}