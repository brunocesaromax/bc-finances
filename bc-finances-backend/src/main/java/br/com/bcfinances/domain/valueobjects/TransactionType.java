package br.com.bcfinances.domain.valueobjects;

import lombok.Getter;

@Getter
public enum TransactionType {

    RECIPE("Receita"),
    EXPENSE("Despesa");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

}