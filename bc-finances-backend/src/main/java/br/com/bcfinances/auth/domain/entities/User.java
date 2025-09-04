package br.com.bcfinances.auth.domain.entities;

import br.com.bcfinances.auth.domain.valueobjects.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Permission> permissions;

    public User(Long id, String name, String email, String password, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.permissions = permissions;
    }
}