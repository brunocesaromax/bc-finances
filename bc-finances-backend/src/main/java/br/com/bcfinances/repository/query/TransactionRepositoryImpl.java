package br.com.bcfinances.repository.query;

import br.com.bcfinances.dto.TransactionStatisticByDay;
import br.com.bcfinances.dto.TransactionStatisticCategory;
import br.com.bcfinances.dto.TransactionStatisticPerson;
import br.com.bcfinances.model.Category_;
import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.model.Transaction_;
import br.com.bcfinances.model.Person_;
import br.com.bcfinances.repository.filter.TransactionFilter;
import br.com.bcfinances.repository.projection.TransactionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Transaction> filterOut(TransactionFilter transactionFilter, Pageable pageable) {

        /*Utilizando criteria do JPA, do hibernate depreciou*/
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Transaction> criteriaQuery = builder.createQuery(Transaction.class);
        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        /*restrições*/
        Predicate[] predicates = getRestrictions(transactionFilter, builder, root);
        criteriaQuery.where(predicates);

        TypedQuery<Transaction> query = manager.createQuery(criteriaQuery);
        addRestrictionsInPagination(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(transactionFilter));
    }

    @Override
    public Page<TransactionSummary> sumUp(TransactionFilter transactionFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TransactionSummary> criteriaQuery = builder.createQuery(TransactionSummary.class);
        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        criteriaQuery.select(builder.construct(TransactionSummary.class,
                root.get(Transaction_.ID),
                root.get(Transaction_.DESCRIPTION),
                root.get(Transaction_.DUE_DAY),
                root.get(Transaction_.PAYDAY),
                root.get(Transaction_.VALUE),
                root.get(Transaction_.TYPE),
                root.get(Transaction_.CATEGORY).get(Category_.NAME),
                root.get(Transaction_.PERSON).get(Person_.NAME)));

        /*restrições*/
        Predicate[] predicates = getRestrictions(transactionFilter, builder, root);
        criteriaQuery.where(predicates);

        TypedQuery<TransactionSummary> query = manager.createQuery(criteriaQuery);
        addRestrictionsInPagination(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(transactionFilter));
    }

    @Override
    public List<TransactionStatisticCategory> findByCategory(LocalDate monthReference) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<TransactionStatisticCategory> criteriaQuery = criteriaBuilder
                .createQuery(TransactionStatisticCategory.class);

        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        criteriaQuery.select(criteriaBuilder.construct(TransactionStatisticCategory.class,
                root.get(Transaction_.CATEGORY),
                criteriaBuilder.sum(root.get(Transaction_.VALUE))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction_.DUE_DAY), firstDay),
                criteriaBuilder.lessThanOrEqualTo(root.get(Transaction_.DUE_DAY), lastDay)
        );

        criteriaQuery.groupBy(root.get(Transaction_.CATEGORY));

        TypedQuery<TransactionStatisticCategory> typedQuery = manager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    @Override
    public List<TransactionStatisticByDay> findByDay(LocalDate monthReference) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<TransactionStatisticByDay> criteriaQuery = criteriaBuilder
                .createQuery(TransactionStatisticByDay.class);

        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        criteriaQuery.select(criteriaBuilder.construct(TransactionStatisticByDay.class,
                root.get(Transaction_.TYPE),
                root.get(Transaction_.DUE_DAY),
                criteriaBuilder.sum(root.get(Transaction_.VALUE))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction_.DUE_DAY), firstDay),
                criteriaBuilder.lessThanOrEqualTo(root.get(Transaction_.DUE_DAY), lastDay)
        );

        criteriaQuery.groupBy(root.get(Transaction_.TYPE), root.get(Transaction_.DUE_DAY));

        TypedQuery<TransactionStatisticByDay> typedQuery = manager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    @Override
    public List<TransactionStatisticPerson> findByPerson(LocalDate start, LocalDate end) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<TransactionStatisticPerson> criteriaQuery = criteriaBuilder
                .createQuery(TransactionStatisticPerson.class);

        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        criteriaQuery.select(criteriaBuilder.construct(TransactionStatisticPerson.class,
                root.get(Transaction_.TYPE),
                root.get(Transaction_.PERSON),
                criteriaBuilder.sum(root.get(Transaction_.VALUE))));

        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction_.DUE_DAY), start),
                criteriaBuilder.lessThanOrEqualTo(root.get(Transaction_.DUE_DAY), end)
        );

        criteriaQuery.groupBy(root.get(Transaction_.TYPE), root.get(Transaction_.PERSON));

        TypedQuery<TransactionStatisticPerson> typedQuery = manager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    private Predicate[] getRestrictions(TransactionFilter transactionFilter, CriteriaBuilder builder, Root<Transaction> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (transactionFilter != null) {

            if (!StringUtils.isEmpty(transactionFilter.getDescription())) {

                // where description like '%Ssga%'
                predicates.add(builder.like(
                        builder.lower(root.get(Transaction_.DESCRIPTION)), "%" + transactionFilter.getDescription().toLowerCase() + "%"));
            }

            if (transactionFilter.getDueDayStart() != null) {
                predicates.add(
                        builder.greaterThanOrEqualTo(root.get(Transaction_.DUE_DAY), transactionFilter.getDueDayStart()));
            }

            if (transactionFilter.getDueDayEnd() != null) {
                predicates.add(
                        builder.lessThanOrEqualTo(root.get(Transaction_.DUE_DAY), transactionFilter.getDueDayEnd()));
            }
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void addRestrictionsInPagination(TypedQuery<?> query, Pageable pageable) {
        long paginaAtual = pageable.getPageNumber();
        long totalRegistrosPorPagina = pageable.getPageSize();
        long primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(Math.toIntExact(primeiroRegistroDaPagina));
        query.setMaxResults(Math.toIntExact(totalRegistrosPorPagina));
    }

    private Long total(TransactionFilter transactionFilter) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Transaction> root = criteriaQuery.from(Transaction.class);

        Predicate[] predicates = getRestrictions(transactionFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);
        criteriaQuery.select(criteriaBuilder.count(root)); // SELECT COUNT(*)

        return manager.createQuery(criteriaQuery).getSingleResult();
    }


}
