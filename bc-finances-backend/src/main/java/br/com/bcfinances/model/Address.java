package br.com.bcfinances.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
