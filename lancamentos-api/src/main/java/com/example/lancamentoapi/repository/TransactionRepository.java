package com.example.lancamentoapi.repository;

import com.example.lancamentoapi.model.Transaction;
import com.example.lancamentoapi.repository.query.TransactionRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryQuery {

    boolean existsByPersonId(Long id);

    List<Transaction> findByDueDateLessThanEqualAndPaydayIsNull(LocalDate date);
}
