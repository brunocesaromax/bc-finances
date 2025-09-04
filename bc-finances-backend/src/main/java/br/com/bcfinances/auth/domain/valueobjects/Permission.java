package br.com.bcfinances.auth.domain.valueobjects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Permission {

    private Long id;
    private String description;

    public Permission(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getAuthority() {
        return description != null ? description.toUpperCase() : null;
    }
}