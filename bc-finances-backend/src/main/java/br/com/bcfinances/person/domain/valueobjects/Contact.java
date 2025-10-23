package br.com.bcfinances.person.domain.valueobjects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Contact {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public Contact(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

}
