package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void execute(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}