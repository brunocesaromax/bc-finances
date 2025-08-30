package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckTransactionExistsByPersonUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public boolean execute(Long personId) {
        return transactionRepository.existsByPersonId(personId);
    }
}