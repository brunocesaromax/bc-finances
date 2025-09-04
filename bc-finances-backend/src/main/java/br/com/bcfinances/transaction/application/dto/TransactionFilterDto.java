package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TransactionFilterDto {

    private String description;
    private LocalDate dueDayStart;
    private LocalDate dueDayEnd;
    private String personName;
    private String categoryName;

}