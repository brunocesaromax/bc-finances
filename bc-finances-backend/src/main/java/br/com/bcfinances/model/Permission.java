package br.com.bcfinances.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
}
