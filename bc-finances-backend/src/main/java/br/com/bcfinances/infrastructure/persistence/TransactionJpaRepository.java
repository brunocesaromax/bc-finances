package br.com.bcfinances.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByPersonId(Long personId);

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT t FROM TransactionEntity t WHERE t.dueDay <= :date AND t.payday IS NULL")
    List<TransactionEntity> findOverdueTransactions(@Param("date") LocalDate date);

    List<TransactionEntity> findByDueDayLessThanEqualAndPaydayIsNull(LocalDate date);

    boolean existsByPersonIdAndPaydayIsNull(Long personId);
}