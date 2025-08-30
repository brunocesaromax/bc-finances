package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.application.dto.TransactionFilterDto;
import br.com.bcfinances.application.dto.TransactionStatisticByDayDto;
import br.com.bcfinances.application.dto.TransactionStatisticCategoryDto;
import br.com.bcfinances.application.dto.TransactionStatisticPersonDto;
import br.com.bcfinances.application.dto.TransactionSummaryDto;
import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.infrastructure.mappers.TransactionEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TransactionRepositoryImpl implements TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionJpaRepository jpaRepository;

    @Autowired
    private TransactionEntityMapper entityMapper;

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
    public List<Transaction> findAll() {
        return jpaRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Transaction> findAll(TransactionFilterDto filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionEntity> criteriaQuery = builder.createQuery(TransactionEntity.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        Predicate[] predicates = getRestrictions(filter, builder, root);
        criteriaQuery.where(predicates);

        TypedQuery<TransactionEntity> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList().stream()
                .map(entityMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Transaction> findAllPaged(TransactionFilterDto filter, int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionEntity> criteriaQuery = builder.createQuery(TransactionEntity.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        Predicate[] predicates = getRestrictions(filter, builder, root);
        criteriaQuery.where(predicates);

        TypedQuery<TransactionEntity> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList().stream()
                .map(entityMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public long count(TransactionFilterDto filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        Predicate[] predicates = getRestrictions(filter, builder, root);
        criteriaQuery.where(predicates);
        criteriaQuery.select(builder.count(root));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<TransactionSummaryDto> findSummary(TransactionFilterDto filter) {
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

        return query.getResultList().stream()
                .map(this::convertSummaryToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransactionSummaryDto> findSummaryPaged(TransactionFilterDto filter, int page, int size) {
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
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList().stream()
                .map(this::convertSummaryToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public long countSummary(TransactionFilterDto filter) {
        return count(filter);
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
                .collect(java.util.stream.Collectors.toList());

        return new PageImpl<>(results, pageable, count(filter));
    }

    @Override
    public List<Transaction> findOverdueTransactions(LocalDate referenceDate) {
        return jpaRepository.findByDueDayLessThanEqualAndPaydayIsNull(referenceDate).stream()
                .map(entityMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransactionStatisticCategoryDto> findStatisticsByCategory(LocalDate monthReference) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionStatisticCategory> criteriaQuery = builder.createQuery(TransactionStatisticCategory.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        criteriaQuery.select(builder.construct(TransactionStatisticCategory.class,
                root.get("category"),
                builder.sum(root.get("value"))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteriaQuery.where(
                builder.greaterThanOrEqualTo(root.get("dueDay"), firstDay),
                builder.lessThanOrEqualTo(root.get("dueDay"), lastDay)
        );

        criteriaQuery.groupBy(root.get("category"));

        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .map(this::convertCategoryStatisticToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransactionStatisticByDayDto> findStatisticsByDay(LocalDate monthReference) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionStatisticDay> criteriaQuery = builder.createQuery(TransactionStatisticDay.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        criteriaQuery.select(builder.construct(TransactionStatisticDay.class,
                root.get("type"),
                root.get("dueDay"),
                builder.sum(root.get("value"))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteriaQuery.where(
                builder.greaterThanOrEqualTo(root.get("dueDay"), firstDay),
                builder.lessThanOrEqualTo(root.get("dueDay"), lastDay)
        );

        criteriaQuery.groupBy(root.get("type"), root.get("dueDay"));

        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .map(this::convertDayStatisticToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransactionStatisticPersonDto> findStatisticsByPerson(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionStatisticPerson> criteriaQuery = builder.createQuery(TransactionStatisticPerson.class);
        Root<TransactionEntity> root = criteriaQuery.from(TransactionEntity.class);

        criteriaQuery.select(builder.construct(TransactionStatisticPerson.class,
                root.get("person"),
                builder.sum(root.get("value"))));

        criteriaQuery.where(
                builder.greaterThanOrEqualTo(root.get("dueDay"), startDate),
                builder.lessThanOrEqualTo(root.get("dueDay"), endDate)
        );

        criteriaQuery.groupBy(root.get("person"));

        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .map(this::convertPersonStatisticToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private Predicate[] getRestrictions(TransactionFilterDto filter, CriteriaBuilder builder, Root<TransactionEntity> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null) {
            if (StringUtils.hasText(filter.getDescription())) {
                predicates.add(builder.like(
                        builder.lower(root.get("description")),
                        "%" + filter.getDescription().toLowerCase() + "%"));
            }

            if (filter.getDueDayFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dueDay"), filter.getDueDayFrom()));
            }

            if (filter.getDueDayTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dueDay"), filter.getDueDayTo()));
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

    private TransactionStatisticCategoryDto convertCategoryStatisticToDto(TransactionStatisticCategory stat) {
        return new TransactionStatisticCategoryDto(
                stat.getCategory().getName(),
                stat.getTotal()
        );
    }

    private TransactionStatisticByDayDto convertDayStatisticToDto(TransactionStatisticDay stat) {
        return new TransactionStatisticByDayDto(
                stat.getDay(),
                stat.getTotal()
        );
    }

    private TransactionStatisticPersonDto convertPersonStatisticToDto(TransactionStatisticPerson stat) {
        return new TransactionStatisticPersonDto(
                "TOTAL", // Tipo fixo para estatística por pessoa
                stat.getPerson().getName(),
                stat.getTotal()
        );
    }
}
