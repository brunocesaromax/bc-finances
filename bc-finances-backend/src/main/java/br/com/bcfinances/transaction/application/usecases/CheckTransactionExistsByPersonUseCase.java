package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckTransactionExistsByPersonUseCase {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public boolean execute(Long personId) {
        return transactionRepository.existsByPersonId(personId);
    }
}