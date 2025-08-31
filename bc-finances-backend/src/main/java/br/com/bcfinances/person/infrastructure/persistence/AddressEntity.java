package br.com.bcfinances.person.infrastructure.persistence;

import br.com.bcfinances.location.infrastructure.persistence.CityEntity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@Setter
@Embeddable
public class AddressEntity {

    private String street;
    private String number;
    private String complement;
    private String neighborhood;

    @Column(name = "zip_code")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;

    public AddressEntity() {}

    public AddressEntity(String street, String number, String complement, String neighborhood, 
                        String zipCode, CityEntity city) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.zipCode = zipCode;
        this.city = city;
    }
}