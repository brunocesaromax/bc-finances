package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.transaction.application.mappers.TransactionMapper;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTransactionsUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponse> execute(TransactionFilterDto filter) {
        List<Transaction> transactions = transactionRepository.findAll(filter);
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> execute(TransactionFilterDto filter, int page, int size) {
        List<Transaction> transactions = transactionRepository.findAllPaged(filter, page, size);
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long count(TransactionFilterDto filter) {
        return transactionRepository.count(filter);
    }
}