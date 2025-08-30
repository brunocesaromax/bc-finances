package br.com.bcfinances.infrastructure.persistence;

public enum TransactionTypeEntity {
    RECIPE("Receita"),
    EXPENSE("Despesa");

    private final String description;

    TransactionTypeEntity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}