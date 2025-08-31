package br.com.bcfinances.transaction.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByPersonId(Long personId);

    List<TransactionEntity> findByDueDayLessThanEqualAndPaydayIsNull(LocalDate date);
}