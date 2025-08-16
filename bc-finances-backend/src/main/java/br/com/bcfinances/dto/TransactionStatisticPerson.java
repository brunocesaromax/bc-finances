package br.com.bcfinances.dto;

import br.com.bcfinances.model.Person;
import br.com.bcfinances.model.TypeLaunch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticPerson {

    private TypeLaunch type;
    private Person person;
    private BigDecimal total;
}
