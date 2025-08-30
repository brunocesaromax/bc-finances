package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.TransactionFilterDto;
import br.com.bcfinances.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.application.mappers.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindTransactionsUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

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