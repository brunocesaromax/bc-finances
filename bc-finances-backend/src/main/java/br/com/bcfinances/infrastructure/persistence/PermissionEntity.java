package br.com.bcfinances.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public PermissionEntity() {}

    public PermissionEntity(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}