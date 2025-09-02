package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.transaction.infrastructure.persistence.TransactionTypeEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class TransactionSummary {

    private Long id;
    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private TransactionTypeEntity type;
    private String category;
    private String person;

    public TransactionSummary(Long id, String description, LocalDate dueDay, LocalDate payday, BigDecimal value, TransactionTypeEntity type, String category, String person) {
        this.id = id;
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.type = type;
        this.category = category;
        this.person = person;
    }
}
