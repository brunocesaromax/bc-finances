package br.com.bcfinances.application.dto;

import java.math.BigDecimal;

public class TransactionStatisticPersonDto {

    private String type;
    private String personName;
    private BigDecimal totalValue;

    public TransactionStatisticPersonDto() {}

    public TransactionStatisticPersonDto(String type, String personName, BigDecimal totalValue) {
        this.type = type;
        this.personName = personName;
        this.totalValue = totalValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}