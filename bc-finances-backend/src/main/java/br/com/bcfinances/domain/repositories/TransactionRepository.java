package br.com.bcfinances.domain.repositories;

public interface TransactionRepository {
    
    boolean existsByPersonId(Long personId);
}