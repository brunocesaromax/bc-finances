package br.com.bcfinances.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionStatisticByDayDto {

    private LocalDate day;
    private BigDecimal totalValue;

    public TransactionStatisticByDayDto() {}

    public TransactionStatisticByDayDto(LocalDate day, BigDecimal totalValue) {
        this.day = day;
        this.totalValue = totalValue;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}