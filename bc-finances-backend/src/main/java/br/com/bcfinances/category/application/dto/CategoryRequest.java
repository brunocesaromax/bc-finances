package br.com.bcfinances.category.application.dto;

import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequest {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private TransactionType transactionType;
}
