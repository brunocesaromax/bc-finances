package br.com.bcfinances.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeLaunch {
    RECIPE("Receita"),
    EXPENSE("Despesa");

    private final String description;
}
