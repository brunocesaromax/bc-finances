package com.example.lancamentoapi.repository.query;

import com.example.lancamentoapi.dto.TransactionStatisticByDay;
import com.example.lancamentoapi.dto.TransactionStatisticCategory;
import com.example.lancamentoapi.dto.TransactionStatisticPerson;
import com.example.lancamentoapi.model.Transaction;
import com.example.lancamentoapi.repository.filter.TransactionFilter;
import com.example.lancamentoapi.repository.projection.TransactionSummary;
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
