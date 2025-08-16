package br.com.bcfinances.repository.query;

import br.com.bcfinances.dto.TransactionStatisticByDay;
import br.com.bcfinances.dto.TransactionStatisticCategory;
import br.com.bcfinances.dto.TransactionStatisticPerson;
import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.repository.filter.TransactionFilter;
import br.com.bcfinances.repository.projection.TransactionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepositoryQuery {

    Page<Transaction> filterOut(TransactionFilter transactionFilter, Pageable pageable);
    Page<TransactionSummary> sumUp(TransactionFilter transactionFilter, Pageable pageable);

    List<TransactionStatisticCategory> findByCategory(LocalDate monthReference);
    List<TransactionStatisticByDay> findByDay(LocalDate monthReference);
    List<TransactionStatisticPerson> findByPerson(LocalDate start, LocalDate end);
}
