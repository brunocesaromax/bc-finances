package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransactionStatisticPersonDto {

    private String type;
    private String personName;
    private BigDecimal totalValue;

    public TransactionStatisticPersonDto(String type, String personName, BigDecimal totalValue) {
        this.type = type;
        this.personName = personName;
        this.totalValue = totalValue;
    }

}