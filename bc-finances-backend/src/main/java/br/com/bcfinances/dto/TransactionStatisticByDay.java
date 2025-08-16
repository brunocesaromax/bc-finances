package br.com.bcfinances.dto;

import br.com.bcfinances.model.TypeLaunch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticByDay {

    private TypeLaunch type;
    private LocalDate day;
    private BigDecimal total;
}
