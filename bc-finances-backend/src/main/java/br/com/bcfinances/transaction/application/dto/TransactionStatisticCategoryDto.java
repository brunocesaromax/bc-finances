package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransactionStatisticCategoryDto {

    private String categoryName;
    private BigDecimal totalValue;

    public TransactionStatisticCategoryDto(String categoryName, BigDecimal totalValue) {
        this.categoryName = categoryName;
        this.totalValue = totalValue;
    }

}