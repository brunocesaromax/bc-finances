package br.com.bcfinances.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "persons")
@Getter
@Setter
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean active;

    @Embedded
    private AddressEntity address;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContactEntity> contacts;

    public PersonEntity() {}

    public PersonEntity(Long id, String name, Boolean active, AddressEntity address, List<ContactEntity> contacts) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.address = address;
        this.contacts = contacts;
    }

    public void updateContacts(List<ContactEntity> contacts) {
        this.getContacts().clear();
        this.getContacts().addAll(contacts);
        this.getContacts().forEach(c -> c.setPerson(this));
    }
}