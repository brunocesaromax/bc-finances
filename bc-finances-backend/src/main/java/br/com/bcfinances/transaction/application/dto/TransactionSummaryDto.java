package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class TransactionSummaryDto {

    private Long id;
    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String type;
    private String categoryName;
    private String personName;

    public TransactionSummaryDto(Long id, String description, LocalDate dueDay, LocalDate payday,
                               BigDecimal value, String type, String categoryName, String personName) {
        this.id = id;
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.type = type;
        this.categoryName = categoryName;
        this.personName = personName;
    }

}