package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TransactionFilterDto {

    private String description;
    private LocalDate dueDayStart;
    private LocalDate dueDayEnd;
    private TransactionType type;
    private Long categoryId;
}
