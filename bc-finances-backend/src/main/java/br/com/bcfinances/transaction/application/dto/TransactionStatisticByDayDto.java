package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class TransactionStatisticByDayDto {

    private LocalDate day;
    private BigDecimal totalValue;

    public TransactionStatisticByDayDto(LocalDate day, BigDecimal totalValue) {
        this.day = day;
        this.totalValue = totalValue;
    }

}