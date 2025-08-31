package br.com.bcfinances.person.application.dto;

import java.util.List;

public class PersonResponse {

    private Long id;
    private String name;
    private Boolean active;
    private AddressResponse address;
    private List<ContactResponse> contacts;

    public PersonResponse() {}

    public PersonResponse(Long id, String name, Boolean active, AddressResponse address, List<ContactResponse> contacts) {
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

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }

    public List<ContactResponse> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactResponse> contacts) {
        this.contacts = contacts;
    }

    public Boolean isInactive() {
        return !this.active;
    }
}