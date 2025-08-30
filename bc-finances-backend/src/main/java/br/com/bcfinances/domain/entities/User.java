package br.com.bcfinances.domain.entities;

import br.com.bcfinances.domain.valueobjects.Permission;
import java.util.List;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Permission> permissions;

    public User() {}

    public User(Long id, String name, String email, String password, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions != null && permissions.stream()
                .anyMatch(p -> permission.equalsIgnoreCase(p.getDescription()));
    }
}