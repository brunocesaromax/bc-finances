package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.person.infrastructure.persistence.PersonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatisticPerson {
    
    private PersonEntity person;
    private BigDecimal total;
}
