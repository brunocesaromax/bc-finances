package br.com.bcfinances.dto;

import br.com.bcfinances.model.Person;
import br.com.bcfinances.model.TypeTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticPerson {

    private TypeTransaction type;
    private Person person;
    private BigDecimal total;
}
