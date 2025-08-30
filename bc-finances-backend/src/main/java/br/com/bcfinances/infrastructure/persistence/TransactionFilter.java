package br.com.bcfinances.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionFilter {
    
    private String description;
    private LocalDate dueDayFrom;
    private LocalDate dueDayTo;
    private Long categoryId;
    private Long personId;
}
