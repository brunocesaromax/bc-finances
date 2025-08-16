package br.com.bcfinances.dto;

import br.com.bcfinances.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticCategory {

    private Category category;
    private BigDecimal total;
}
