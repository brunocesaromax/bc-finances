package br.com.bcfinances.category.application.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CategoryRequest {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
}
