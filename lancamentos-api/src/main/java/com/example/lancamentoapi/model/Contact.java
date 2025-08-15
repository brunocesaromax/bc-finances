package com.example.lancamentoapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "contacts")
@Data
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 3, max = 50)
	private String name;

	@NotNull
	@Email
	private String email;

	@NotEmpty
	@Size(max = 20)
	private String phone;

	@ManyToOne
	@JoinColumn(name="person_id")
	private Person person;
}
