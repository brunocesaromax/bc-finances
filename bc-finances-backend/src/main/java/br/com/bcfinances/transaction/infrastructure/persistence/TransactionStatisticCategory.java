package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.category.infrastructure.persistence.CategoryEntity;
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
    
    private CategoryEntity category;
    private BigDecimal total;
}
