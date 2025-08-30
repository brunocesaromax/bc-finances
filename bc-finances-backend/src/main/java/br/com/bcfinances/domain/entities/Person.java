package br.com.bcfinances.domain.entities;

import br.com.bcfinances.domain.valueobjects.Address;
import br.com.bcfinances.domain.valueobjects.Contact;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Boolean isInactive() {
        return !this.active;
    }

    public void updateContacts(List<Contact> contacts) {
        this.getContacts().clear();
        this.getContacts().addAll(contacts);
    }
}