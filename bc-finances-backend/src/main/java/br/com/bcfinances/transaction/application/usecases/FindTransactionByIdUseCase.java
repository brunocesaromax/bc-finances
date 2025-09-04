package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.application.mappers.TransactionMapper;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindTransactionByIdUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public Optional<TransactionResponse> execute(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(transactionMapper::toResponse);
    }
}