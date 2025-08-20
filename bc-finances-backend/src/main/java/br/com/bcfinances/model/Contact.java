package br.com.bcfinances.model;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


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
