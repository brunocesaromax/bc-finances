package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.application.mappers.TransactionMapper;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindOverdueTransactionsUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

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