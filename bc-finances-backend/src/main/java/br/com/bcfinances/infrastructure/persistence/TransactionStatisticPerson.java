package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.Person;
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
    
    private Person person;
    private BigDecimal total;
}
