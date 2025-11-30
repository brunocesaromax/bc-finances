package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void execute(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        transactionRepository.deleteById(id);
    }
}
