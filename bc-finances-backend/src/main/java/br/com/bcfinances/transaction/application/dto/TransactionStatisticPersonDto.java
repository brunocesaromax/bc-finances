package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.person.infrastructure.persistence.PersonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class TransactionStatisticPersonDto {

    private String type;
    private PersonEntity person;
    private BigDecimal total;
    
}