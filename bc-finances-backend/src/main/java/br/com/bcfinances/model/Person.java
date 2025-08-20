package br.com.bcfinances.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "persons")
@Data
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 50)
	private String name;
	
	@NotNull
	private Boolean active;
	
	@Embedded
	private Address address;

	@Valid
	@JsonIgnoreProperties("person")
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts;

	@Transient
	@JsonIgnore
	public Boolean isInactive(){
		return !this.active;
	}

	public void updateContacts(List<Contact> contacts) {
		this.getContacts().clear();
		this.getContacts().addAll(contacts);
		this.getContacts().forEach(c -> c.setPerson(this));
	}
}
