package br.com.bcfinances.category.domain.entities;

import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private TransactionType transactionType;
    
    public Category(String name, TransactionType transactionType) {
        this.name = name;
        this.transactionType = transactionType;
    }
    
    public boolean isValid() {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 50
                && transactionType != null;
    }
}
