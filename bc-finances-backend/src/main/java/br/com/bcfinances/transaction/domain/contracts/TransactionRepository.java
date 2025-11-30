package br.com.bcfinances.transaction.domain.contracts;

import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.TransactionSummaryDto;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);

    Page<TransactionSummaryDto> findWithFilter(TransactionFilterDto filter, Pageable pageable);
    List<Transaction> findOverdueTransactions(LocalDate referenceDate);
}
