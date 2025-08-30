package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final br.com.bcfinances.repository.TransactionRepository legacyTransactionRepository;

    public TransactionRepositoryImpl(br.com.bcfinances.repository.TransactionRepository legacyTransactionRepository) {
        this.legacyTransactionRepository = legacyTransactionRepository;
    }

    @Override
    public boolean existsByPersonId(Long personId) {
        return legacyTransactionRepository.existsByPersonId(personId);
    }
}