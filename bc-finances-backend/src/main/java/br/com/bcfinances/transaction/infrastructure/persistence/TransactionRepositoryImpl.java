package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.TransactionSummaryDto;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.infrastructure.mappers.TransactionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

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
    public Page<TransactionSummaryDto> findWithFilter(TransactionFilterDto filter, Pageable pageable) {
        Specification<TransactionEntity> specification = buildSpecification(filter);

        Page<TransactionEntity> page = jpaRepository.findAll(specification, pageable);

        List<TransactionSummaryDto> results = page.getContent().stream()
                .map(entityMapper::toDomain)
                .map(this::convertSummaryToDto)
                .toList();

        return new PageImpl<>(results, pageable, page.getTotalElements());
    }

    @Override
    public List<Transaction> findOverdueTransactions(LocalDate referenceDate) {
        return jpaRepository.findByDueDayLessThanEqualAndPaydayIsNull(referenceDate).stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    private Specification<TransactionEntity> buildSpecification(TransactionFilterDto filter) {
        return (root, query, builder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

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

                if (filter.getType() != null) {
                    predicates.add(builder.equal(root.get("type"),
                            TransactionTypeEntity.valueOf(filter.getType().name())));
                }

                if (filter.getCategoryId() != null) {
                    predicates.add(builder.equal(root.get("category").get("id"), filter.getCategoryId()));
                }
            }

            query.distinct(true);
            query.orderBy(builder.desc(root.get("dueDay")), builder.desc(root.get("id")));

            return builder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private TransactionSummaryDto convertSummaryToDto(Transaction transaction) {
        List<String> tags = transaction.getTags() != null
                ? transaction.getTags().stream()
                .map(Tag::getName)
                .filter(name -> name != null && !name.isBlank())
                .toList()
                : List.of();

        boolean hasAttachments = transaction.getAttachments() != null && !transaction.getAttachments().isEmpty();

        return new TransactionSummaryDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getDueDay(),
                transaction.getPayday(),
                transaction.getValue(),
                transaction.getType().name(),
                transaction.getCategory() != null ? transaction.getCategory().getName() : null,
                tags,
                hasAttachments
        );
    }
}
