package br.com.bcfinances.category.domain.contracts;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findByTransactionType(TransactionType transactionType);
}
