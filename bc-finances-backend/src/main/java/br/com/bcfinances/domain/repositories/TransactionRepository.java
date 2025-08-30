package br.com.bcfinances.domain.repositories;

import br.com.bcfinances.application.dto.TransactionFilterDto;
import br.com.bcfinances.application.dto.TransactionStatisticByDayDto;
import br.com.bcfinances.application.dto.TransactionStatisticCategoryDto;
import br.com.bcfinances.application.dto.TransactionStatisticPersonDto;
import br.com.bcfinances.application.dto.TransactionSummaryDto;
import br.com.bcfinances.domain.entities.Transaction;
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
    List<Transaction> findAll();
    List<Transaction> findAll(TransactionFilterDto filter);
    List<Transaction> findAllPaged(TransactionFilterDto filter, int page, int size);
    long count(TransactionFilterDto filter);
    
    List<TransactionSummaryDto> findSummary(TransactionFilterDto filter);
    List<TransactionSummaryDto> findSummaryPaged(TransactionFilterDto filter, int page, int size);
    long countSummary(TransactionFilterDto filter);
    
    boolean existsByPersonId(Long personId);

    Page<TransactionSummaryDto> findWithFilter(TransactionFilterDto filter, Pageable pageable);
    List<Transaction> findOverdueTransactions(LocalDate referenceDate);

    List<TransactionStatisticCategoryDto> findStatisticsByCategory(LocalDate monthReference);
    List<TransactionStatisticByDayDto> findStatisticsByDay(LocalDate monthReference);
    List<TransactionStatisticPersonDto> findStatisticsByPerson(LocalDate startDate, LocalDate endDate);
}