package br.com.bcfinances.transaction.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {

    Optional<TransactionEntity> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    List<TransactionEntity> findByDueDayLessThanEqualAndPaydayIsNullAndDeletedAtIsNull(LocalDate date);

    @Override
    @EntityGraph(attributePaths = {"category", "tags", "attachments"})
    Page<TransactionEntity> findAll(Specification<TransactionEntity> spec, Pageable pageable);
}
