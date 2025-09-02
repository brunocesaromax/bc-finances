package br.com.bcfinances.transaction.infrastructure.persistence;

import lombok.Getter;

@Getter
public enum TransactionTypeEntity {
    RECIPE("Receita"),
    EXPENSE("Despesa");

    private final String description;

    TransactionTypeEntity(String description) {
        this.description = description;
    }

}