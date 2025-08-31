package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TransactionFilterDto {

    private String description;
    private LocalDate dueDayFrom;
    private LocalDate dueDayTo;
    private String personName;
    private String categoryName;

}