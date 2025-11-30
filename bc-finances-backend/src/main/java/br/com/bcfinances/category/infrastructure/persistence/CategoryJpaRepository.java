package br.com.bcfinances.category.infrastructure.persistence;

import br.com.bcfinances.transaction.infrastructure.persistence.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByTransactionType(TransactionTypeEntity transactionType);
}
