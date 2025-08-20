package br.com.bcfinances.model;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Embeddable
class Address {

    private String street;
    private String number;
    private String complement;
    private String neighborhood;

    @Column(name = "zip_code")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
