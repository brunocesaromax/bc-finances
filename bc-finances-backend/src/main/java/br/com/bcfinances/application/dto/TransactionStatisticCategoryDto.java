package br.com.bcfinances.application.dto;

import java.math.BigDecimal;

public class TransactionStatisticCategoryDto {

    private String categoryName;
    private BigDecimal totalValue;

    public TransactionStatisticCategoryDto() {}

    public TransactionStatisticCategoryDto(String categoryName, BigDecimal totalValue) {
        this.categoryName = categoryName;
        this.totalValue = totalValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}