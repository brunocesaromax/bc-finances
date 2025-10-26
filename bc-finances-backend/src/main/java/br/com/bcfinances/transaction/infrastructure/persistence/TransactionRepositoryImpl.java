package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.TransactionSummaryDto;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.infrastructure.mappers.TransactionEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final TransactionJpaRepository jpaRepository;
    private final TransactionEntityMapper entityMapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = entityMapper.toEntity(transaction);
        TransactionEntity savedEntity = jpaRepository.save(entity);
        return entityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return jpaRepository.findById(id)
                .map(entityMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByPersonId(Long personId) {
        return jpaRepository.existsByPersonId(personId);
    }

    @Override
    public Page<TransactionSummaryDto> findWithFilter(TransactionFilterDto filter, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionSummary> criteriaQuery = builder.createQuery(TransactionSummary.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        criteriaQuery.select(builder.construct(TransactionSummary.class,
                root.get("id"),
                root.get("description"),
                root.get("dueDay"),
                root.get("payday"),
                root.get("value"),
                root.get("type"),
                root.get("category").get("name"),
                root.get("person").get("name")));

        Predicate[] predicates = getRestrictions(filter, builder, root);
        criteriaQuery.where(predicates);

        TypedQuery<TransactionSummary> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<TransactionSummaryDto> results = query.getResultList().stream()
                .map(this::convertSummaryToDto)
                .toList();

        return new PageImpl<>(results, pageable, countTransactions(filter));
    }

    @Override
    public List<Transaction> findOverdueTransactions(LocalDate referenceDate) {
        return jpaRepository.findByDueDayLessThanEqualAndPaydayIsNull(referenceDate).stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    private long countTransactions(TransactionFilterDto filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        Predicate[] predicates = getRestrictions(filter, builder, root);
        criteriaQuery.where(predicates);
        criteriaQuery.select(builder.count(root));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private Predicate[] getRestrictions(TransactionFilterDto filter, CriteriaBuilder builder, Root<TransactionEntity> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null) {
            if (StringUtils.hasText(filter.getDescription())) {
                predicates.add(builder.like(
                        builder.lower(root.get("description")),
                        "%" + filter.getDescription().toLowerCase() + "%"));
            }

            if (filter.getDueDayStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dueDay"), filter.getDueDayStart()));
            }

            if (filter.getDueDayEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dueDay"), filter.getDueDayEnd()));
            }
        }

        return predicates.toArray(new Predicate[0]);
    }

    private TransactionSummaryDto convertSummaryToDto(TransactionSummary summary) {
        return new TransactionSummaryDto(
                summary.getId(),
                summary.getDescription(),
                summary.getDueDay(),
                summary.getPayday(),
                summary.getValue(),
                summary.getType().name(),
                summary.getCategory(),
                summary.getPerson()
        );
    }

}
