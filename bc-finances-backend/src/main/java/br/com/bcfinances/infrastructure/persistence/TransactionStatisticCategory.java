package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatisticCategory {
    
    private Category category;
    private BigDecimal total;
}
