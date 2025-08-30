package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.application.mappers.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindOverdueTransactionsUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponse> execute(LocalDate date) {
        List<Transaction> overdueTransactions = transactionRepository.findOverdueTransactions(date);
        return overdueTransactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> execute() {
        return execute(LocalDate.now());
    }
}