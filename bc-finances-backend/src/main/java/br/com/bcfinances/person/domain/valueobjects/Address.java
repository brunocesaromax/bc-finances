package br.com.bcfinances.person.domain.valueobjects;

import br.com.bcfinances.location.domain.entities.City;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
    private City city;

    public Address(String street, String number, String complement, String neighborhood, String zipCode, City city) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.zipCode = zipCode;
        this.city = city;
    }

}