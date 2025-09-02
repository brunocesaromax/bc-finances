package br.com.bcfinances.transaction.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatisticDay {
    
    private TransactionTypeEntity type;
    private LocalDate day;
    private BigDecimal total;
}
