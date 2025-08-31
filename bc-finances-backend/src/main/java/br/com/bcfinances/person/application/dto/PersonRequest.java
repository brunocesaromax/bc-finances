package br.com.bcfinances.person.application.dto;

import java.util.List;

public class PersonRequest {

    private String name;
    private Boolean active;
    private AddressRequest address;
    private List<ContactRequest> contacts;

    public PersonRequest() {}

    public PersonRequest(String name, Boolean active, AddressRequest address, List<ContactRequest> contacts) {
        this.name = name;
        this.active = active;
        this.address = address;
        this.contacts = contacts;
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

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public List<ContactRequest> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactRequest> contacts) {
        this.contacts = contacts;
    }

    public static class AddressRequest {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String zipCode;
        private Long cityId;

        public AddressRequest() {}

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        
        public String getComplement() { return complement; }
        public void setComplement(String complement) { this.complement = complement; }
        
        public String getNeighborhood() { return neighborhood; }
        public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
        
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        
        public Long getCityId() { return cityId; }
        public void setCityId(Long cityId) { this.cityId = cityId; }
    }

    public static class ContactRequest {
        private Long id;
        private String name;
        private String email;
        private String phone;

        public ContactRequest() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
}