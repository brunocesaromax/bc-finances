package br.com.bcfinances.transaction.application.dto.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class TransactionRequest {

    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String observation;
    private String type;
    private Long categoryId;
    private Long personId;
    private String attachment;

}