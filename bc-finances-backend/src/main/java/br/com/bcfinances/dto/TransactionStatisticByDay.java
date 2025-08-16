package br.com.bcfinances.dto;

import br.com.bcfinances.model.TypeTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticByDay {

    private TypeTransaction type;
    private LocalDate day;
    private BigDecimal total;
}
