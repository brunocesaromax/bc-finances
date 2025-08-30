package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.application.mappers.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FindTransactionByIdUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public Optional<TransactionResponse> execute(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(transactionMapper::toResponse);
    }
}