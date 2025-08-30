package br.com.bcfinances.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    public ContactEntity() {}

    public ContactEntity(Long id, String name, String email, String phone, PersonEntity person) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.person = person;
    }
}